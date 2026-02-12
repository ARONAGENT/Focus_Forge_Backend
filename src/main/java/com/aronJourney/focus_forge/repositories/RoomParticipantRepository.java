package com.aronJourney.focus_forge.repositories;

import com.aronJourney.focus_forge.entities.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipant,Long> {


    List<RoomParticipant> findByStudyRoomId(Long studyRoomId);
    Long countByStudyRoomId(Long studyRoomId);
    Optional<RoomParticipant> findByStudyRoomIdAndUserId(Long studyRoomId, Long userId);

    List<RoomParticipant> findByUserId(Long id);
}
