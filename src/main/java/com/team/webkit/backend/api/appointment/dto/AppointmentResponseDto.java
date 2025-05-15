package com.team.webkit.backend.api.appointment.dto;

import com.team.webkit.backend.api.appointment.entity.Appointment;
import com.team.webkit.backend.api.appointment.entity.Appointment.AppointmentStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDto {

    private Long id;
    private Integer userId;
    private String userNickname;
    private Integer vetId;
    private Integer petId;
    private String petName;
    private LocalDateTime appointmentTime;
    private Boolean isInPerson;
    private String purpose;
    private AppointmentStatus status;
    private String vetOpinion;

    public static AppointmentResponseDto fromEntity(Appointment appointment) {
        return new AppointmentResponseDto(
            appointment.getId(),
            appointment.getUser() != null ? appointment.getUser().getId() : null,
            appointment.getUser() != null ? appointment.getUser().getNickname() : null,
            appointment.getVetId(),
            appointment.getPet() != null ? appointment.getPet().getId() : null,
            appointment.getPet() != null ? appointment.getPet().getName() : null,
            appointment.getAppointmentTime(),
            appointment.getIsInPerson(),
            appointment.getPurpose(),
            appointment.getStatus(),
            appointment.getVetOpinion()
        );
    }


}
