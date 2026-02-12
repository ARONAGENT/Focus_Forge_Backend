package com.aronJourney.focus_forge.repositories;

import com.aronJourney.focus_forge.entities.StudyRoom;
import com.aronJourney.focus_forge.entities.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom,Long> {
    List<StudyRoom> findByCreatorId(Long creatorId);
    List<StudyRoom> findByStatus(RoomStatus status);
}
