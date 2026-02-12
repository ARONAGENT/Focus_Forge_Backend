package com.aronJourney.focus_forge.repositories;

import com.aronJourney.focus_forge.entities.ChatMessage;
import com.aronJourney.focus_forge.entities.enums.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    List<ChatMessage> findByStudyRoomIdOrderBySentAtAsc(Long studyRoomId);
    Long countByStudyRoomId(Long studyRoomId);
    Long countByStudyRoomIdAndMessageType(Long studyRoomId, MessageType messageType);

    List<ChatMessage> findByStudyRoomIdAndMessageType(Long roomId, MessageType messageType);


}
