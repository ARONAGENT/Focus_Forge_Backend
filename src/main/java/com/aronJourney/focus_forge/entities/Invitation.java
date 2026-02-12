package com.aronJourney.focus_forge.entities;
import com.aronJourney.focus_forge.entities.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "study_room_id", nullable = false)
    private StudyRoom studyRoom;

    @ManyToOne
    @JoinColumn(name = "invitee_id", nullable = false)
    private User invitee;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status = InvitationStatus.PENDING; // PENDING, ACCEPTED, REJECTED

    private LocalDateTime sentAt;

    private LocalDateTime respondedAt;

    @PrePersist
    protected void onCreate() {
        sentAt = LocalDateTime.now();
    }
}