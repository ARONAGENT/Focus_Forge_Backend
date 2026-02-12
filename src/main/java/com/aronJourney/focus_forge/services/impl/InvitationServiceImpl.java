package com.aronJourney.focus_forge.services.impl;

import com.aronJourney.focus_forge.dto.invitation.InvitationAcceptResponse;
import com.aronJourney.focus_forge.dto.invitation.InvitationRequest;
import com.aronJourney.focus_forge.dto.invitation.InvitationResponse;
import com.aronJourney.focus_forge.entities.Invitation;
import com.aronJourney.focus_forge.entities.RoomParticipant;
import com.aronJourney.focus_forge.entities.StudyRoom;
import com.aronJourney.focus_forge.entities.User;
import com.aronJourney.focus_forge.entities.enums.InvitationStatus;
import com.aronJourney.focus_forge.entities.enums.RoomStatus;
import com.aronJourney.focus_forge.repositories.InvitationRepository;
import com.aronJourney.focus_forge.repositories.RoomParticipantRepository;
import com.aronJourney.focus_forge.repositories.StudyRoomRepository;
import com.aronJourney.focus_forge.repositories.UserRepository;
import com.aronJourney.focus_forge.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.aronJourney.focus_forge.utils.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final UserRepository userRepository;
    private final RoomParticipantRepository participantRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public InvitationResponse sendInvitation(InvitationRequest request) {
        // 1. Get logged-in user from Security
        User currentUser = getCurrentUser();

        StudyRoom studyRoom = studyRoomRepository.findById(request.getStudyRoomId())
                .orElseThrow(() -> new RuntimeException("Study room not found"));

        // 2. Verify only creator can send invitations
        if (!studyRoom.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Only the creator can send invitations");
        }

        // 3. Verify room is not completed
        if (studyRoom.getStatus() == RoomStatus.COMPLETED) {
            throw new RuntimeException("Cannot send invitation to completed room");
        }

        User invitee = userRepository.findById(request.getInviteeId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 4. Check if user is trying to invite themselves
        if (invitee.getId().equals(currentUser.getId())) {
            throw new RuntimeException("You cannot invite yourself");
        }

        // 5. Check if invitation already exists
        invitationRepository.findByStudyRoomIdAndInviteeId(
                        request.getStudyRoomId(), request.getInviteeId())
                .ifPresent(inv -> {
                    throw new RuntimeException("Invitation already sent to this user");
                });

        Invitation invitation = new Invitation();
        invitation.setStudyRoom(studyRoom);
        invitation.setInvitee(invitee);
        invitation.setStatus(InvitationStatus.PENDING);
        Invitation invitation1 =  invitationRepository.save(invitation);
        return modelMapper.map(invitation1,InvitationResponse.class);
    }

    @Override
    @Transactional
    public void respondToInvitation(InvitationAcceptResponse response) {
        // 1. Get logged-in user from Security
        User currentUser =getCurrentUser();

        Invitation invitation = invitationRepository.findById(response.getInvitationId())
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        // 2. Verify the invitation is for current user
        if (!invitation.getInvitee().getId().equals(currentUser.getId())) {
            throw new RuntimeException("This invitation is not for you");
        }

        // 3. Check if already responded
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new RuntimeException("Invitation already responded to");
        }

        // 4. Check if room is still joinable
        if (invitation.getStudyRoom().getStatus() == RoomStatus.COMPLETED) {
            throw new RuntimeException("This study room has already ended");
        }

        invitation.setStatus( (response.getAccept() ?
                InvitationStatus.ACCEPTED : InvitationStatus.REJECTED));
        invitation.setRespondedAt(LocalDateTime.now());

        invitationRepository.save(invitation);

        // If accepted, add user as participant
        if (response.getAccept()) {
            // Check if already a participant
            if (participantRepository.findByStudyRoomIdAndUserId(
                    invitation.getStudyRoom().getId(), currentUser.getId()).isEmpty()) {

                RoomParticipant participant = new RoomParticipant();
                participant.setStudyRoom(invitation.getStudyRoom());
                participant.setUser(currentUser);
                participantRepository.save(participant);
            }
        }
    }

    @Override
    public List<InvitationResponse> getPendingInvitations() {

        User currentUser = getCurrentUser();

        List<Invitation> invitationList =
                invitationRepository.findByInviteeIdAndStatus(
                        currentUser.getId(),
                        InvitationStatus.PENDING
                );

        return invitationList.stream()
                .map(invitation -> modelMapper.map(invitation, InvitationResponse.class))
                .toList();
    }


    @Override
    public List<InvitationResponse> getSentInvitations(Long roomId) {

        User currentUser = getCurrentUser();

        StudyRoom studyRoom = studyRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Study room not found"));

        if (!studyRoom.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Only creator can view sent invitations");
        }

        List<Invitation> invitationList =
                invitationRepository.findByStudyRoomId(roomId);

        return invitationList.stream()
                .map(invitation -> modelMapper.map(invitation, InvitationResponse.class))
                .toList();
    }

}
