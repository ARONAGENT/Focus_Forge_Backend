package com.aronJourney.focus_forge.services.impl;

import com.aronJourney.focus_forge.dto.LeaderBoardDto;
import com.aronJourney.focus_forge.dto.profilesAndReports.CommitDto;
import com.aronJourney.focus_forge.dto.studyRoom.StudySessionDto;
import com.aronJourney.focus_forge.dto.studyRoom.StudySessionRequest;
import com.aronJourney.focus_forge.dto.profilesAndReports.WeeklyReportResponseDto;
import com.aronJourney.focus_forge.entities.*;
import com.aronJourney.focus_forge.repositories.*;
import com.aronJourney.focus_forge.services.StudySessionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.aronJourney.focus_forge.utils.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudySessionService {
    private final StudySessionRepository sessionRepo;
    private final UserRepository userRepo;
    private final StreakRepository streakRepo;
    private final ModelMapper modelMapper;
    private final WeekReportRepository weekReportRepository;
    private final CommitRepository commitRepository;
    private final GamificationService gamificationService;
    private final LeaderBoardRepository leaderBoardRepository;


    @Override
    @Transactional
    public StudySessionDto addStudySession(StudySessionRequest req) {
        User user = getCurrentUser();
        StudySession session = new StudySession();
        session.setUser(user);
        session.setDate(LocalDate.now());
        session.setSubject(req.getSubject());
        session.setPlatform(req.getPlatform());
        session.setDuration(req.getDuration());
        session.setNotes(req.getNotes());

        Integer points = gamificationService.calculateSessionPoints(req.getDuration());
        session.setPoints(points);

        StudySession studySession=sessionRepo.save(session);

        // weekly report update
        updateWeeklyReport(user, req.getDuration());

        // update commits
        saveCommit(user, studySession);

        // update leader board
        updateLeaderBoard(user, points, req.getDuration());

        updateStreak(user);
        return modelMapper.map(studySession,StudySessionDto.class);
    }


    // ADD THIS METHOD
    private void updateLeaderBoard(User user, Integer points, Integer duration) {
        LeaderBoard leaderBoard = leaderBoardRepository.findByUser(user)
                .orElseGet(() -> {
                    LeaderBoard lb = new LeaderBoard();
                    lb.setUser(user);
                    lb.setPoints(0);
                    lb.setDuration(0);
                    return lb;
                });

        leaderBoard.setPoints(leaderBoard.getPoints() + points);
        leaderBoard.setDuration(leaderBoard.getDuration() + duration);

        leaderBoardRepository.save(leaderBoard);
    }


    @Override
    public WeeklyReportResponseDto getMyWeeklyReport() {
        User user = getCurrentUser();
        LocalDate weekStart = getWeekStart();
        LocalDate today = LocalDate.now();

        WeeklyReport report = weekReportRepository
                .findByUserAndWeekStart(user, weekStart)
                .orElseThrow(() -> new RuntimeException("No report found"));

        List<CommitDto> commitDtos =
                commitRepository.findWeeklyCommits(user, weekStart, today)
                        .stream()
                        .map(c -> new CommitDto(
                                c.getDate(),
                                c.getMessage()
                        ))
                        .toList();

        double dailyAverage = report.getTotalStudyTime() / 7.0;

        Integer totalPoints = sessionRepo.getTotalPointsByUser(user);


        return WeeklyReportResponseDto.builder()
                .weekStart(report.getWeekStart())
                .totalStudyTime(report.getTotalStudyTime())
                .dailyAverage(dailyAverage)
                .consistency(report.getConsistency())
                .commits(commitDtos)
                .totalPoints(totalPoints)
                .build();
    }

    @Override
    public List<LeaderBoardDto> getLeaderBoard(int limit) {
        List<LeaderBoard> leaderboards = leaderBoardRepository
                .findAllByOrderByPointsDescDurationDesc(
                        PageRequest.of(0, limit)
                );

        // Calculate rank based on position
        List<LeaderBoardDto> result = new ArrayList<>();
        for (int i = 0; i < leaderboards.size(); i++) {
            LeaderBoard lb = leaderboards.get(i);
            result.add(LeaderBoardDto.builder()
                    .userId(lb.getUser().getId())
                    .username(lb.getUser().getUsername())
                    .points(lb.getPoints())
                    .duration(lb.getDuration())
                    .rank(i + 1) // rank calculated here
                    .build());
        }

        return result;
    }

    @Override
    public void populateLeaderBoard() {
        // Get all users
        List<User> allUsers = userRepo.findAll();

        for (User user : allUsers) {
            // Check if leaderboard entry already exists
            Optional<LeaderBoard> existing = leaderBoardRepository.findByUser(user);
            if (existing.isPresent()) {
                continue; // skip if already exists
            }

            // Calculate total points and duration from study sessions
            Integer totalPoints = sessionRepo.getTotalPointsByUser(user);
            Integer totalDuration = sessionRepo.getTotalDurationByUser(user);

            // Create new leaderboard entry
            LeaderBoard leaderBoard = new LeaderBoard();
            leaderBoard.setUser(user);
            leaderBoard.setPoints(totalPoints != null ? totalPoints : 0);
            leaderBoard.setDuration(totalDuration != null ? totalDuration : 0);

            leaderBoardRepository.save(leaderBoard);
        }
    }


    private void updateStreak(User user) {

        LocalDate today = LocalDate.now();

        Streak streak = streakRepo.findByUser(user)
                .orElseGet(() -> {
                    Streak s = new Streak();
                    s.setUser(user);
                    s.setCurrentStreak(0);
                    s.setLongestStreak(0);
                    return s;
                });

        streak.setCurrentStreak(Optional.ofNullable(streak.getCurrentStreak()).orElse(0));
        streak.setLongestStreak(Optional.ofNullable(streak.getLongestStreak()).orElse(0));

        if (streak.getLastStudyDate() == null) {
            streak.setCurrentStreak(1);
        } else {
            long gap = ChronoUnit.DAYS.between(streak.getLastStudyDate(), today);

            if (gap == 0) {
                return; // already studied today
            }

            if (gap == 1) {
                streak.setCurrentStreak(streak.getCurrentStreak() + 1);
            } else if (gap > 1) {
                streak.setCurrentStreak(1);
            }
        }

        if (streak.getCurrentStreak() > streak.getLongestStreak()) {
            streak.setLongestStreak(streak.getCurrentStreak());
        }

        streak.setLastStudyDate(today);

        streakRepo.save(streak);
    }




    private void updateWeeklyReport(User user, Integer duration) {

        LocalDate weekStart = getWeekStart();


        WeeklyReport report = weekReportRepository
                .findByUserAndWeekStart(user, weekStart)
                .orElseGet(() -> {
                    WeeklyReport r = new WeeklyReport();
                    r.setUser(user);
                    r.setWeekStart(weekStart);
                    r.setTotalStudyTime(0);
                    r.setConsistency(0.0);
                    return r;
                });

        // Update total time
        report.setTotalStudyTime(
                report.getTotalStudyTime() + duration
        );

        // Calculate consistency (distinct study days this week)
        long studyDays = sessionRepo
                .countDistinctDatesByUserAndWeek(user, weekStart, LocalDate.now());

        double consistency = (studyDays / 7.0) * 100;
        report.setConsistency(consistency);


        weekReportRepository.save(report);
    }


    private LocalDate getWeekStart() {
        return LocalDate.now().with(DayOfWeek.MONDAY);
    }


    private void saveCommit(User user, StudySession session) {

        Commit commit = new Commit();
        commit.setUser(user);
        commit.setDate(LocalDate.now());
        commit.setMessage(
                "Studied " + session.getSubject() +
                        " for " + session.getDuration() + " mins on " +
                        session.getPlatform()
        );

        commitRepository.save(commit);
    }





}
