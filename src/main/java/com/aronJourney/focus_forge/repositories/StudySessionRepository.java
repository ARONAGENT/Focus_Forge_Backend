package com.aronJourney.focus_forge.repositories;

import com.aronJourney.focus_forge.entities.StudySession;
import com.aronJourney.focus_forge.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession,Long> {
    Arrays findByUser(User user);

    List<StudySession> findByUserAndDateBetween(
            User user,
            LocalDate start,
            LocalDate end
    );

    @Query("""
    SELECT s FROM StudySession s
    WHERE s.user.id = :userId
    ORDER BY s.date DESC
    """)
    List<StudySession> findAllByUserId(Long userId);


    @Query("""
   SELECT COUNT(DISTINCT s.date)
   FROM StudySession s
   WHERE s.user = :user
   AND s.date BETWEEN :start AND :end
    """)
    long countDistinctDatesByUserAndWeek(User user, LocalDate start, LocalDate end);


    @Query("SELECT COALESCE(SUM(s.points), 0) FROM StudySession s WHERE s.user = :user")
    Integer getTotalPointsByUser(@Param("user") User user);

    @Query("""
       SELECT COALESCE(SUM(s.points), 0)
       FROM StudySession s
       WHERE s.user = :user
       AND s.date BETWEEN :start AND :end
       """)
    Integer getWeeklyPoints(
            @Param("user") User user,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );


    @Query("SELECT COALESCE(SUM(s.duration), 0) FROM StudySession s WHERE s.user = :user")
    Integer getTotalDurationByUser(@Param("user") User user);

}
