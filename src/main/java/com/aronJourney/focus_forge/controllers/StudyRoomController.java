package com.aronJourney.focus_forge.controllers;

import com.aronJourney.focus_forge.dto.chat.MessageDto;
import com.aronJourney.focus_forge.dto.studyRoom.CreateStudyRoomRequest;
import com.aronJourney.focus_forge.dto.studyRoom.StudyRoomResponse;
import com.aronJourney.focus_forge.entities.StudyRoom;
import com.aronJourney.focus_forge.services.StudyRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/study-rooms")
@RequiredArgsConstructor
@Tag(name = "Study Room Module", description = "Manage Study Rooms - Create, Start, End, View")
public class StudyRoomController {

    private final StudyRoomService studyRoomService;

    @Operation(
            summary = "Create Study Room",
            description = "Creates a new study room. The logged-in user becomes the creator."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Study room created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/create")
    public ResponseEntity<StudyRoomResponse> createStudyRoom(
            @Valid @RequestBody CreateStudyRoomRequest request) {
        // User is automatically fetched from SecurityContext
        StudyRoomResponse room = studyRoomService.createStudyRoom(request);
        return ResponseEntity.ok(room);
    }


    @Operation(
            summary = "Start Study Room",
            description = "Only the creator can start the study room."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Study room started successfully"),
            @ApiResponse(responseCode = "403", description = "Only creator can start the room")
    })
    @PostMapping("/{roomId}/start")
    public ResponseEntity<MessageDto> startStudyRoom(@PathVariable Long roomId) {
        studyRoomService.startStudyRoom(roomId);
        return ResponseEntity.ok(new MessageDto("Study room started successfully"));
    }

    @Operation(
            summary = "End Study Room",
            description = "Only the creator can end the study room. A session record will be created automatically."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Study room ended successfully"),
            @ApiResponse(responseCode = "403", description = "Only creator can end the room")
    })
    @PostMapping("/{roomId}/end")
    public ResponseEntity<MessageDto> endStudyRoom(@PathVariable Long roomId) {
        studyRoomService.endStudyRoom(roomId);
        return ResponseEntity.ok(new MessageDto("Study room ended and record created"));
    }

    @Operation(
            summary = "Get My Created Rooms",
            description = "Fetch all study rooms created by the logged-in user."
    )
    @GetMapping("/my-rooms")
    public ResponseEntity<List<StudyRoomResponse>> getMyCreatedRooms() {
        List<StudyRoomResponse> rooms = studyRoomService.getMyCreatedRooms();
        return ResponseEntity.ok(rooms);
    }


    @Operation(
            summary = "Get Participating Rooms",
            description = "Fetch all study rooms where the logged-in user is a participant."
    )
    @GetMapping("/participating")
    public ResponseEntity<List<StudyRoomResponse>> getMyParticipatingRooms() {
        List<StudyRoomResponse> rooms = studyRoomService.getMyParticipatingRooms();
        return ResponseEntity.ok(rooms);
    }

    @Operation(
            summary = "Get Study Room Details",
            description = "Fetch detailed information about a specific study room."
    )
    @GetMapping("/{roomId}")
    public ResponseEntity<StudyRoomResponse> getRoomDetails(@PathVariable Long roomId) {
        StudyRoomResponse room = studyRoomService.getRoomDetails(roomId);
        return ResponseEntity.ok(room);
    }


    @Operation(
            summary = "Join Study Room",
            description = "Join an active study room you've been invited to."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully joined the room"),
            @ApiResponse(responseCode = "403", description = "Room is not active or you weren't invited")
    })
    @PostMapping("/{roomId}/join")
    public ResponseEntity<MessageDto> joinStudyRoom(@PathVariable Long roomId) {
        studyRoomService.joinStudyRoom(roomId);
        return ResponseEntity.ok(new MessageDto("Successfully joined the study room"));
    }

    @Operation(
            summary = "Leave Study Room",
            description = "Leave a study room you're participating in."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully left the room"),
            @ApiResponse(responseCode = "403", description = "You are not a participant of this room")
    })
    @PostMapping("/{roomId}/leave")
    public ResponseEntity<MessageDto> leaveStudyRoom(@PathVariable Long roomId) {
        studyRoomService.leaveStudyRoom(roomId);
        return ResponseEntity.ok(new MessageDto("Successfully left the study room"));
    }
}