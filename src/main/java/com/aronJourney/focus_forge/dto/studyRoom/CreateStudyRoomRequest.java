package com.aronJourney.focus_forge.dto.studyRoom;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class CreateStudyRoomRequest {

        @NotBlank(message = "Topic name is required")
        private String topicName;

        @NotNull(message = "Duration is required")
        @Min(value = 15, message = "Duration must be at least 15 minutes")
        @Max(value = 480, message = "Duration cannot exceed 8 hours")
        private Integer durationMinutes;
}

