package com.aronJourney.focus_forge.dto.studyRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudySessionDto {
    private LocalDate date;
    private String subject;
    private String platform;
    private Integer duration;
    private String notes;
}
