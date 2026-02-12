package com.aronJourney.focus_forge.services;

import com.aronJourney.focus_forge.dto.LeaderBoardDto;
import com.aronJourney.focus_forge.dto.studyRoom.StudySessionDto;
import com.aronJourney.focus_forge.dto.studyRoom.StudySessionRequest;
import com.aronJourney.focus_forge.dto.profilesAndReports.WeeklyReportResponseDto;

import java.util.List;

public interface StudySessionService {
     StudySessionDto addStudySession(StudySessionRequest req);
    WeeklyReportResponseDto getMyWeeklyReport();
    List<LeaderBoardDto> getLeaderBoard(int limit);
    void populateLeaderBoard();
}
