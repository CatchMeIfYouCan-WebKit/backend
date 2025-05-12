package com.team.webkit.backend.api.appointment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "vet_id")
    private Integer vetId;

    @Column(name = "pet_id")
    private Integer petId;

    @Column(name = "visit_count")
    private Integer visitCount = 0;

    @Column(name = "appointment_time")
    private LocalDateTime appointmentTime;

    @Column(name = "is_in_person")
    private Boolean isInPerson = true;

    private String purpose;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.WAITING;

    @Column(name = "vet_opinion")
    private String vetOpinion;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // ✅ 내부 Enum 정의
    public enum AppointmentStatus {
        WAITING,
        RESERVED,
        CANCELLED,
        COMPLETED
    }
}
