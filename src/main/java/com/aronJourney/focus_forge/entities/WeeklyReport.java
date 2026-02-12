package com.aronJourney.focus_forge.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class WeeklyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate weekStart;
    private Integer totalStudyTime;
    private Double consistency;

    @ManyToOne
    private User user;
}
