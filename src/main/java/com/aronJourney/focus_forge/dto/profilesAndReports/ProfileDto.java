package com.aronJourney.focus_forge.dto.profilesAndReports;

import com.aronJourney.focus_forge.dto.auth.UserDto;
import com.aronJourney.focus_forge.dto.studyRoom.StudySessionDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProfileDto {
    private Integer age;
    private String goal;
    private String skills;
    private String targetJob;
    private UserDto user;
    private List<StudySessionDto> sessions;
}
