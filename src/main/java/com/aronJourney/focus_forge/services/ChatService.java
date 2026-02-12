package com.aronJourney.focus_forge.services;

import com.aronJourney.focus_forge.dto.chat.MessageDto;

public interface ChatService {
    MessageDto askAI(String msg);
}
