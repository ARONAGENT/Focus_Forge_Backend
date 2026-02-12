package com.aronJourney.focus_forge.dto.profilesAndReports;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for creating or updating user profile")
public class ProfileRequestDto {
    @Schema(example = "22", description = "User age")
    private Integer age;

    @Schema(example = "Crack Product Based Company in 6 months", description = "User career goal")
    private String goal;

    @Schema(example = "Java, Spring Boot, DSA", description = "Current skills")
    private String skills;

    @Schema(example = "Backend Developer", description = "Target job role")
    private String targetJob;
}
