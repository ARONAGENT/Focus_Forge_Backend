package com.aronJourney.focus_forge.dto.invitation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InvitationAcceptResponse {
    @NotNull
    Long invitationId;
    Boolean accept;
}
