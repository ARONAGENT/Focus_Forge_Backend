package com.aronJourney.focus_forge.dto.invitation;

import com.aronJourney.focus_forge.dto.auth.UserDto;
import com.aronJourney.focus_forge.dto.studyRoom.StudyRoomDto;
import com.aronJourney.focus_forge.entities.enums.InvitationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvitationResponse {
    private Long id;

    private StudyRoomDto studyRoom;

    private UserDto invitee;

    private InvitationStatus status = InvitationStatus.PENDING; // PENDING, ACCEPTED, REJECTED

    private LocalDateTime sentAt;

    private LocalDateTime respondedAt;
}
