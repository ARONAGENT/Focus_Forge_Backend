package com.aronJourney.focus_forge.services.impl;

import com.aronJourney.focus_forge.dto.studyRoom.CreateStudyRoomRequest;
import com.aronJourney.focus_forge.dto.studyRoom.StudyRoomResponse;
import com.aronJourney.focus_forge.entities.RoomParticipant;
import com.aronJourney.focus_forge.entities.StudyRoom;
import com.aronJourney.focus_forge.entities.StudySessionRecord;
import com.aronJourney.focus_forge.entities.User;
import com.aronJourney.focus_forge.entities.enums.MessageType;
import com.aronJourney.focus_forge.entities.enums.RoomStatus;
import com.aronJourney.focus_forge.exceptions.ResourceNotFoundException;
import com.aronJourney.focus_forge.repositories.*;
import com.aronJourney.focus_forge.services.StudyRoomService;
import com.aronJourney.focus_forge.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.aronJourney.focus_forge.utils.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class StudyRoomServiceImpl implements StudyRoomService {


    private final StudyRoomRepository studyRoomRepository;
    private final UserRepository userRepository;
    private final RoomParticipantRepository participantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final StudySessionRecordRepository sessionRecordRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public StudyRoomResponse createStudyRoom(CreateStudyRoomRequest request ) {
        // 1. Get logged-in user from Security
        User creator = getCurrentUser();

        StudyRoom studyRoom = new StudyRoom();
        studyRoom.setTopicName(request.getTopicName());
        studyRoom.setCreator(creator);
        studyRoom.setDurationMinutes(request.getDurationMinutes());
        studyRoom.setStatus(RoomStatus.PENDING);
        StudyRoom savedRoom = studyRoomRepository.save(studyRoom);

        // Automatically add creator as participant
        RoomParticipant creatorParticipant = new RoomParticipant();
        creatorParticipant.setStudyRoom(savedRoom);
        creatorParticipant.setUser(creator);
        participantRepository.save(creatorParticipant);
        return modelMapper.map(savedRoom, StudyRoomResponse.class);
    }

    @Override
    @Transactional
    public void startStudyRoom(Long roomId) {
        User currentUser = getCurrentUser();

        StudyRoom room = studyRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Study room not found"));

        // 2. Verify only creator can start the room
        if (!room.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Only the creator can start this study room");
        }

        if (room.getStatus() != RoomStatus.PENDING) {
            throw new RuntimeException("Study room is not in pending state");
        }

        room.setStatus(RoomStatus.ACTIVE);
        room.setStartTime(LocalDateTime.now());
        room.setEndTime(LocalDateTime.now().plusMinutes(room.getDurationMinutes()));

        studyRoomRepository.save(room);
    }

    @Override
    @Transactional
    public void endStudyRoom(Long roomId) {
        User currentUser = getCurrentUser();

        StudyRoom room = studyRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Study room not found"));

        // 2. Verify only creator can end the room
        if (!room.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Only the creator can end this study room");
        }

        if (room.getStatus() != RoomStatus.ACTIVE) {
            throw new RuntimeException("Study room is not active");
        }

        room.setStatus(RoomStatus.COMPLETED);
        room.setEndTime(LocalDateTime.now());
        studyRoomRepository.save(room);

        // Create session record
        createSessionRecord(room);

    }

    @Override
    public List<StudyRoomResponse> getMyCreatedRooms() {
        User currentUser = getCurrentUser();
        List<StudyRoom> rooms= studyRoomRepository.findByCreatorId(currentUser.getId());
       return rooms.stream()
                .map(room -> modelMapper.map(room, StudyRoomResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudyRoomResponse> getMyParticipatingRooms() {
        User currentUser = getCurrentUser();

        List<RoomParticipant> myParticipations = participantRepository
                .findByUserId(currentUser.getId()); // ✅ Find by USER ID
        return myParticipations.stream()
                .map(RoomParticipant::getStudyRoom)
                .map(room -> modelMapper.map(room, StudyRoomResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public StudyRoomResponse getRoomDetails(Long roomId) {
        User currentUser = getCurrentUser();

        StudyRoom room = studyRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Study room not found"));

        // Verify user is participant or creator
        boolean isCreator = room.getCreator().getId().equals(currentUser.getId());
        boolean isParticipant = participantRepository
                .findByStudyRoomIdAndUserId(roomId, currentUser.getId()).isPresent();

        if (!isCreator && !isParticipant) {
            throw new RuntimeException("You don't have access to this study room");
        }
        return modelMapper.map(room,StudyRoomResponse.class);
    }


    private void createSessionRecord(StudyRoom room) {
        List<RoomParticipant> participants = participantRepository.findByStudyRoomId(room.getId());
        Long totalMessages = chatMessageRepository.countByStudyRoomId(room.getId());
        Long totalDoubts = chatMessageRepository.countByStudyRoomIdAndMessageType(
                room.getId(), MessageType.DOUBT);

        String participantNames = participants.stream()
                .map(p -> p.getUser().getUsername())
                .collect(Collectors.joining(", "));

        StudySessionRecord record = new StudySessionRecord();
        record.setStudyRoom(room);
        record.setTopicName(room.getTopicName());
        record.setTotalParticipants(participants.size());
        record.setTotalMessages(totalMessages.intValue());
        record.setTotalDoubts(totalDoubts.intValue());
        record.setDurationMinutes(room.getDurationMinutes());
        record.setSessionStartTime(room.getStartTime());
        record.setSessionEndTime(room.getEndTime());
        record.setParticipantNames(participantNames);

        sessionRecordRepository.save(record);
    }


    @Override
    public void joinStudyRoom(Long roomId) {
        User currentUser = getCurrentUser();
        StudyRoom room = studyRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Study room not found"));

        // Verify room is active
        if (room.getStatus() != RoomStatus.ACTIVE) {
            throw new IllegalStateException("Room is not active");
        }

        // Verify user was invited (has a participant record)
        RoomParticipant participant = participantRepository
                .findByStudyRoomIdAndUserId(roomId, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("You haven't been invited to this room"));

        // Mark as "joined" if you have a hasJoined field, or just verify they can enter
        // participant.setHasJoined(true);
        // participantRepository.save(participant);
    }

    @Override
    public void leaveStudyRoom(Long roomId) {
        User currentUser = getCurrentUser();
        StudyRoom room = studyRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Study room not found"));

        // Don't allow creator to leave using this endpoint
        if (room.getCreator().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("Room creator cannot leave. Use 'End Room' instead.");
        }

        // Find and remove participant record
        RoomParticipant participant = participantRepository
                .findByStudyRoomIdAndUserId(roomId, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("You are not a participant of this room"));

        participantRepository.delete(participant);

    }




}
