package com.aronJourney.focus_forge.dto.studyRoom;

import com.aronJourney.focus_forge.dto.auth.UserDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudyRoomResponse {
    private Long id;
    private String topicName;
    private UserDto creator;
    private Integer durationMinutes;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
