package com.aronJourney.focus_forge.tool;

import com.aronJourney.focus_forge.dto.profilesAndReports.WeeklyReportResponseDto;
import com.aronJourney.focus_forge.services.StudySessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WeeklyReportToolCall {


    private final StudySessionService sessionService;


    @Tool(description = "Get the Weekly Report and Analysis and predict what improvement needs")
    public WeeklyReportResponseDto getWeeklyReport(){
        return sessionService.getMyWeeklyReport();
    }

}
