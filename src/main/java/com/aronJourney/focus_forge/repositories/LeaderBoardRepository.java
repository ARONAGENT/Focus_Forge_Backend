package com.aronJourney.focus_forge.repositories;

import com.aronJourney.focus_forge.dto.LeaderBoardDto;
import com.aronJourney.focus_forge.entities.LeaderBoard;
import com.aronJourney.focus_forge.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LeaderBoardRepository extends JpaRepository<LeaderBoard,Long> {

    Optional<LeaderBoard> findByUser(User user);

    // Order by points DESC, then duration DESC (highest first)
    List<LeaderBoard> findAllByOrderByPointsDescDurationDesc(Pageable pageable);

}
