package com.aronJourney.focus_forge.controllers;

import com.aronJourney.focus_forge.dto.chat.ChatGroupedResponse;
import com.aronJourney.focus_forge.dto.chat.ChatMessageDto;
import com.aronJourney.focus_forge.dto.chat.ChatMessageRequest;
import com.aronJourney.focus_forge.services.ChattingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Tag(name = "Chat Module", description = "Study Room Chat and Doubt Discussion APIs")
public class ChattingController {

    private final ChattingService chatService;

    @Operation(
            summary = "Send Message",
            description = "Send a chat or doubt message to a study room. User is extracted from JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @ApiResponse(responseCode = "403", description = "User not part of the study room"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/send")
    public ResponseEntity<ChatMessageDto> sendMessage(
            @Valid @RequestBody ChatMessageRequest request) {
        // User is automatically fetched from SecurityContext
        ChatMessageDto message = chatService.sendMessage(request);
        return ResponseEntity.ok(message);
    }

    @Operation(
            summary = "Get All Room Messages",
            description = "Fetch all grouped chat messages for a study room"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages fetched successfully"),
            @ApiResponse(responseCode = "403", description = "User not part of the study room")
    })
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ChatGroupedResponse>> getRoomMessages(@PathVariable Long roomId) {
        List<ChatGroupedResponse> messages = chatService.getRoomMessages(roomId);
        return ResponseEntity.ok(messages);
    }


    @Operation(
            summary = "Get Room Doubts",
            description = "Fetch only DOUBT type messages grouped by sender for a study room"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doubts fetched successfully"),
            @ApiResponse(responseCode = "403", description = "User not part of the study room")
    })

    @GetMapping("/room/{roomId}/doubts")
    public ResponseEntity<List<ChatGroupedResponse>> getRoomDoubts(@PathVariable Long roomId) {
        List<ChatGroupedResponse> doubts = chatService.getRoomDoubts(roomId);
        return ResponseEntity.ok(doubts);
    }
}