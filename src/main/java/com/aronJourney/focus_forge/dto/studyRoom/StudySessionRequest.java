package com.aronJourney.focus_forge.dto.studyRoom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request body to log a study session")
public class StudySessionRequest {

    @Schema(example = "Data Structures", description = "Subject studied")
    private String subject;

    @Schema(example = "LeetCode", description = "Platform used for study")
    private String platform;

    @Schema(example = "120", description = "Duration of study in minutes")
    private Integer duration;

    @Schema(example = "Solved 5 array problems", description = "Notes about the session")
    private String notes;
}
