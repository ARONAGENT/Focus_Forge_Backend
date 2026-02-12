package com.aronJourney.focus_forge.dto.profilesAndReports;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CommitDto {
    private LocalDate date;
    private String message;
}

