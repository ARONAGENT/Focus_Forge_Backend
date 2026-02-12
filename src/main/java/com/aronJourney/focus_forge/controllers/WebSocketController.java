package com.aronJourney.focus_forge.controllers;


import com.aronJourney.focus_forge.dto.chat.ChatMessageDto;
import com.aronJourney.focus_forge.dto.chat.ChatMessageRequest;
import com.aronJourney.focus_forge.services.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final ChattingService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage/{roomId}")
    public void sendMessage(
            @Payload ChatMessageRequest message,
            @DestinationVariable Long roomId) {

        // User is automatically fetched from SecurityContext in service
        ChatMessageDto savedMessage = chatService.sendMessage(message);

        // Broadcast to all subscribers of this room
        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                savedMessage
        );
    }
}
