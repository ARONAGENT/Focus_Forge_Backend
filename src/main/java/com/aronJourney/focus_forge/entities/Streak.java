package com.aronJourney.focus_forge.entities;

import com.aronJourney.focus_forge.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Streak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer currentStreak = 0;
    private Integer longestStreak = 0;
    private Long totalMinutes = 0L;
    private Long totalSessions = 0L;

    private LocalDate lastStudyDate;

    @OneToOne
    private User user;
}
