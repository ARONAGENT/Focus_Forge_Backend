package com.aronJourney.focus_forge.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatGroupedResponse {
    private Long senderId;
    private String senderName;
    private List<String> messages;
}
