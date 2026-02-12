package com.aronJourney.focus_forge.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_session_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudySessionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "study_room_id", nullable = false)
    private StudyRoom studyRoom;

    private String topicName;

    private Integer totalParticipants;

    private Integer totalMessages;

    private Integer totalDoubts;

    private Integer durationMinutes;

    private LocalDateTime sessionStartTime;

    private LocalDateTime sessionEndTime;

    @Column(length = 2000)
    private String participantNames; // Comma-separated names

    private LocalDateTime recordedAt;

    @PrePersist
    protected void onCreate() {
        recordedAt = LocalDateTime.now();
    }
}