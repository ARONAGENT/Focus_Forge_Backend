package com.aronJourney.focus_forge.controllers;

import com.aronJourney.focus_forge.dto.chat.MessageDto;
import com.aronJourney.focus_forge.dto.invitation.InvitationAcceptResponse;
import com.aronJourney.focus_forge.dto.invitation.InvitationRequest;
import com.aronJourney.focus_forge.dto.invitation.InvitationResponse;
import com.aronJourney.focus_forge.services.InvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/invitations")
@RequiredArgsConstructor
@Tag(name = "Invitations", description = "APIs for managing study room invitations")
public class InvitationController {

    private final InvitationService invitationService;

    @Operation(summary = "Send invitation to a user for a study room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PostMapping("/send")
    public ResponseEntity<InvitationResponse> sendInvitation(
            @Valid @RequestBody InvitationRequest request) {
        // User is automatically fetched from SecurityContext
        InvitationResponse invitation = invitationService.sendInvitation(request);
        return ResponseEntity.ok(invitation);
    }

    @Operation(summary = "Accept or reject an invitation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation response recorded"),
            @ApiResponse(responseCode = "404", description = "Invitation not found")
    })
    @PostMapping("/respond")
    public ResponseEntity<MessageDto> respondToInvitation(
            @Valid @RequestBody InvitationAcceptResponse response) {
        invitationService.respondToInvitation(response);
        return ResponseEntity.ok(new MessageDto("Invitation response recorded"));
    }

    @Operation(summary = "Get all pending invitations for logged-in user")
    @GetMapping("/pending")
    public ResponseEntity<List<InvitationResponse>> getMyPendingInvitations() {
        // User is automatically fetched from SecurityContext
        List<InvitationResponse> invitations = invitationService.getPendingInvitations();
        return ResponseEntity.ok(invitations);
    }

    @Operation(summary = "Get all invitations sent for a specific room (creator only)")
    @GetMapping("/sent/{roomId}")
    public ResponseEntity<List<InvitationResponse>> getSentInvitations(@PathVariable Long roomId) {
        List<InvitationResponse> invitations = invitationService.getSentInvitations(roomId);
        return ResponseEntity.ok(invitations);
    }
}