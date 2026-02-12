package com.aronJourney.focus_forge.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer age;
    private String goal;
    private String skills;
    private String targetJob;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}