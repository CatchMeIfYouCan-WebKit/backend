package com.team.webkit.backend.api.appointment.dto;

import com.team.webkit.backend.api.appointment.entity.Appointment;
import com.team.webkit.backend.api.member.entity.Member;
import com.team.webkit.backend.api.pet.entity.Pet;
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

    public Appointment toEntity(Member user, Pet pet, int visitCount) {
        Appointment appointment = new Appointment();
        appointment.setVetId(this.vetId); // vetId는 필드 그대로 존재한다고 가정
        appointment.setAppointmentTime(this.appointmentTime);
        appointment.setIsInPerson(this.isInPerson);
        appointment.setPurpose(this.purpose);
        appointment.setVisitCount(visitCount);

        // 연관관계 주입
        appointment.setUser(user);
        appointment.setPet(pet);

        return appointment;
    }


}
