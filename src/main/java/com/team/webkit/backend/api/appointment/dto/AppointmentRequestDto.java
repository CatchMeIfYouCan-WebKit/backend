package com.team.webkit.backend.api.appointment.dto;

import com.team.webkit.backend.api.appointment.entity.Appointment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDto {

    private Integer userId;
    private Integer vetId;
    private Integer petId;
    private LocalDateTime appointmentTime;
    private Boolean isInPerson;
    private String purpose;

    public Appointment toEntity(int visitCount) {
        Appointment appointment = new Appointment();
        appointment.setUserId(this.userId);
        appointment.setVetId(this.vetId);
        appointment.setPetId(this.petId);
        appointment.setAppointmentTime(this.appointmentTime);
        appointment.setIsInPerson(this.isInPerson);
        appointment.setPurpose(this.purpose);
        appointment.setVisitCount(visitCount);
        return appointment;
    }

}
