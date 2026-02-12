package com.aronJourney.focus_forge.dto.chat;

import com.aronJourney.focus_forge.dto.auth.UserDto;
import com.aronJourney.focus_forge.dto.studyRoom.StudyRoomDto;
import com.aronJourney.focus_forge.entities.enums.MessageType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto {

    private Long id;
    private StudyRoomDto studyRoom;
    private UserDto sender;

    private String message;
    private MessageType messageType = MessageType.CHAT; // CHAT, DOUBT

    private LocalDateTime sentAt;

}
