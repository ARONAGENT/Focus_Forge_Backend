package com.aronJourney.focus_forge.dto.profilesAndReports;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Weekly study report for the user")
public class WeeklyReportResponseDto {

    @Schema(example = "2026-02-10", description = "Start date of the week")
    private LocalDate weekStart;

    @Schema(example = "840", description = "Total study time in minutes for the week")
    private Integer totalStudyTime;

    @Schema(example = "120", description = "total credits points after completing sessions")
    private Integer totalPoints;  // ✅ add this

    @Schema(example = "85.5", description = "Consistency percentage for the week")
    private Double consistency;

    @Schema(example = "50.5", description = "daily average work ")
    private Double dailyAverage;


    @Schema(description = "List of daily commits")
    private List<CommitDto> commits;

}
