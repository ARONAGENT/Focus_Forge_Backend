package com.aronJourney.focus_forge.services;

import com.aronJourney.focus_forge.dto.invitation.InvitationAcceptResponse;
import com.aronJourney.focus_forge.dto.invitation.InvitationRequest;
import com.aronJourney.focus_forge.dto.invitation.InvitationResponse;
import com.aronJourney.focus_forge.entities.Invitation;

import java.util.List;

public interface InvitationService {
    InvitationResponse sendInvitation(InvitationRequest request);

     void respondToInvitation(InvitationAcceptResponse response);

     List<InvitationResponse> getPendingInvitations();
      List<InvitationResponse> getSentInvitations(Long roomId);
}

