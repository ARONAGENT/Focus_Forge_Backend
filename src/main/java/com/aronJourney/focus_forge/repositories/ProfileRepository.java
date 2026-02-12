package com.aronJourney.focus_forge.repositories;

import com.aronJourney.focus_forge.entities.Profile;
import com.aronJourney.focus_forge.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {


    boolean existsByUser(User user);
    Optional<Profile> findByUser(User user);
}
