package com.aronJourney.focus_forge.repositories;

import com.aronJourney.focus_forge.entities.Invitation;
import com.aronJourney.focus_forge.entities.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation,Long> {
    List<Invitation> findByInviteeIdAndStatus(Long inviteeId, InvitationStatus status);
    List<Invitation> findByStudyRoomId(Long studyRoomId);
    Optional<Invitation> findByStudyRoomIdAndInviteeId(Long studyRoomId, Long inviteeId);

}
