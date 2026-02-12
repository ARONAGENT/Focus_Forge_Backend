package com.aronJourney.focus_forge.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChatMessageRequest {
    @NotNull(message = "Study room ID is required")
    private Long studyRoomId;

    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    private String message;

    @NotNull(message = "Message type is required")
    private String messageType; // "CHAT" or "DOUBT"
}
