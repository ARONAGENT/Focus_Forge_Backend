package com.aronJourney.focus_forge.repositories;

import com.aronJourney.focus_forge.entities.Streak;
import com.aronJourney.focus_forge.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreakRepository extends JpaRepository<Streak,Long> {
    Optional<Streak> findByUser(User user);
}
