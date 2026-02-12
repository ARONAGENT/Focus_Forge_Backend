package com.aronJourney.focus_forge.dto.invitation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class InvitationRequest {
    @NotNull(message = "Study room ID is required")
    private Long studyRoomId;

    @NotNull(message = "Invitee ID is required")
    private Long inviteeId;
}
