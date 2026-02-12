package com.aronJourney.focus_forge.repositories;

import com.aronJourney.focus_forge.entities.Commit;
import com.aronJourney.focus_forge.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommitRepository extends JpaRepository<Commit,Long> {
    @Query("""
   SELECT c FROM Commit c
   WHERE c.user = :user
   AND c.date BETWEEN :start AND :end
   ORDER BY c.createdAt DESC
    """)
    List<Commit> findWeeklyCommits(User user, LocalDate start, LocalDate end);

}
