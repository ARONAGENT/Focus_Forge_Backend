package com.aronJourney.focus_forge.controllers;

import com.aronJourney.focus_forge.dto.profilesAndReports.WeeklyReportResponseDto;
import com.aronJourney.focus_forge.services.impl.StudyServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Report APIs")
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class WeeklyReportController {

    private final StudyServiceImpl studyServiceImpl;
    @GetMapping("/weekly")
    @Operation(summary = "Get weekly study report of logged-in user")
    public ResponseEntity<WeeklyReportResponseDto> weeklyReport() {
        return ResponseEntity.ok(studyServiceImpl.getMyWeeklyReport());
    }
}
