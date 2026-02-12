package com.aronJourney.focus_forge.services;

import com.aronJourney.focus_forge.dto.chat.ChatGroupedResponse;
import com.aronJourney.focus_forge.dto.chat.ChatMessageDto;
import com.aronJourney.focus_forge.dto.chat.ChatMessageRequest;
import com.aronJourney.focus_forge.entities.ChatMessage;

import java.util.List;

public interface ChattingService {
     ChatMessageDto sendMessage(ChatMessageRequest request);

    List<ChatGroupedResponse> getRoomMessages(Long roomId);
     List<ChatGroupedResponse> getRoomDoubts(Long roomId);
}
