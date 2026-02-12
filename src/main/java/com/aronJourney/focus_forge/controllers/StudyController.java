package com.aronJourney.focus_forge.controllers;


import com.aronJourney.focus_forge.dto.LeaderBoardDto;
import com.aronJourney.focus_forge.dto.chat.MessageDto;
import com.aronJourney.focus_forge.dto.studyRoom.StudySessionDto;
import com.aronJourney.focus_forge.dto.studyRoom.StudySessionRequest;
import com.aronJourney.focus_forge.services.StudySessionService;
import com.aronJourney.focus_forge.services.impl.StudyServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Study APIs")
@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

    private final StudySessionService studySessionService;

    @Operation(summary = "Add new study session")
    @PostMapping(name = "/add")
    public ResponseEntity<StudySessionDto> addSession(
            @RequestBody StudySessionRequest request) {

        StudySessionDto studySessionDto= studySessionService.addStudySession(request);
        return new ResponseEntity<>(studySessionDto, HttpStatus.CREATED);
    }

    @GetMapping("/getLeaderBoard")
    public ResponseEntity<List<LeaderBoardDto>> getLeaderBoard(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(studySessionService.getLeaderBoard(limit));
    }
//
//    @PostMapping("/populate")
//    public ResponseEntity<MessageDto> populateLeaderBoard() {
//        studySessionService.populateLeaderBoard();
//        return ResponseEntity.ok(new MessageDto("LeaderBoard populated successfully!"));
//    }

}
