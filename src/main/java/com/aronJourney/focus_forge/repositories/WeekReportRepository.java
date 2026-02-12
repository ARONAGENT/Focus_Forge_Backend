package com.aronJourney.focus_forge.repositories;

import com.aronJourney.focus_forge.entities.User;
import com.aronJourney.focus_forge.entities.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WeekReportRepository extends JpaRepository<WeeklyReport,Long> {
    Optional<WeeklyReport> findByUserAndWeekStart(User user, LocalDate weekStart);

}
