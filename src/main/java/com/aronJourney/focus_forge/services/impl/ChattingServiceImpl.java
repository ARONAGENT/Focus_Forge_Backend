package com.aronJourney.focus_forge.services.impl;

import com.aronJourney.focus_forge.dto.chat.ChatGroupedResponse;
import com.aronJourney.focus_forge.dto.chat.ChatMessageDto;
import com.aronJourney.focus_forge.dto.chat.ChatMessageRequest;
import com.aronJourney.focus_forge.entities.ChatMessage;
import com.aronJourney.focus_forge.entities.StudyRoom;
import com.aronJourney.focus_forge.entities.User;
import com.aronJourney.focus_forge.entities.enums.MessageType;
import com.aronJourney.focus_forge.entities.enums.RoomStatus;
import com.aronJourney.focus_forge.repositories.ChatMessageRepository;
import com.aronJourney.focus_forge.repositories.RoomParticipantRepository;
import com.aronJourney.focus_forge.repositories.StudyRoomRepository;
import com.aronJourney.focus_forge.repositories.UserRepository;
import com.aronJourney.focus_forge.services.ChattingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.aronJourney.focus_forge.utils.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class ChattingServiceImpl implements ChattingService {


    private final ChatMessageRepository chatMessageRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final UserRepository userRepository;
    private final RoomParticipantRepository participantRepository;
    private final ModelMapper modelMapper;


    @Override
    public ChatMessageDto sendMessage(ChatMessageRequest request) {
        // 1. Get logged-in user from Security
        User sender = getCurrentUser();

        StudyRoom studyRoom = studyRoomRepository.findById(request.getStudyRoomId())
                .orElseThrow(() -> new RuntimeException("Study room not found"));

        // 2. Verify study room is active
        if (studyRoom.getStatus() != RoomStatus.ACTIVE) {
            throw new RuntimeException("Study room is not active");
        }

        // 3. Verify user is a participant
        participantRepository.findByStudyRoomIdAndUserId(
                        request.getStudyRoomId(), sender.getId())
                .orElseThrow(() -> new RuntimeException(
                        "You are not a participant of this study room"));

        ChatMessage message = new ChatMessage();
        message.setStudyRoom(studyRoom);
        message.setSender(sender);
        message.setMessage(request.getMessage());
        message.setMessageType(MessageType.valueOf(
                request.getMessageType().toUpperCase()));

        ChatMessage chatMessage= chatMessageRepository.save(message);
        return modelMapper.map(chatMessage,ChatMessageDto.class);

    }

    @Override
    public List<ChatGroupedResponse> getRoomMessages(Long roomId) {

        User currentUser = getCurrentUser();

        participantRepository
                .findByStudyRoomIdAndUserId(roomId, currentUser.getId())
                .orElseThrow(() ->
                        new RuntimeException("You are not a participant of this study room"));

        List<ChatMessage> chatMessages =
                chatMessageRepository.findByStudyRoomIdOrderBySentAtAsc(roomId);

        Map<User, List<ChatMessage>> grouped =
                chatMessages.stream()
                        .collect(Collectors.groupingBy(ChatMessage::getSender));

        return grouped.entrySet().stream()
                .map(entry -> new ChatGroupedResponse(
                        entry.getKey().getId(),
                        entry.getKey().getUsername(),
                        entry.getValue().stream()
                                .map(ChatMessage::getMessage)
                                .toList()
                ))
                .toList();
    }



    @Override
    public List<ChatGroupedResponse> getRoomDoubts(Long roomId) {

        User currentUser = getCurrentUser();

        participantRepository
                .findByStudyRoomIdAndUserId(roomId, currentUser.getId())
                .orElseThrow(() ->
                        new RuntimeException("You are not a participant of this study room"));

        List<ChatMessage> chatMessages =
                chatMessageRepository.findByStudyRoomIdAndMessageType(
                        roomId, MessageType.DOUBT);
        Map<User, List<ChatMessage>> grouped =
                chatMessages.stream()
                        .collect(Collectors.groupingBy(ChatMessage::getSender));

        return grouped.entrySet().stream()
                .map(entry -> new ChatGroupedResponse(
                        entry.getKey().getId(),
                        entry.getKey().getUsername(),
                        entry.getValue().stream()
                                .map(ChatMessage::getMessage)
                                .toList()
                ))
                .toList();
    }



}
