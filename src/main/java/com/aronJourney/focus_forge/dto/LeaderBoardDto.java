package com.aronJourney.focus_forge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaderBoardDto {
    private Long userId;
    private String username;
    private Integer points;
    private Integer duration;
    private Integer rank;
}
