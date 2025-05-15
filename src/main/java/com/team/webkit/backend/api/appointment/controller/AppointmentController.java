package com.team.webkit.backend.api.appointment.controller;

import com.team.webkit.backend.api.appointment.dto.AppointmentResponseDto;
import com.team.webkit.backend.api.appointment.entity.Appointment;
import com.team.webkit.backend.api.appointment.entity.Appointment.AppointmentStatus;
import com.team.webkit.backend.api.appointment.service.AppointmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // 예약 신청
//    @PostMapping
//    public ResponseEntity<AppointmentResponseDto> create(@RequestBody AppointmentRequestDto dto) {
//        Appointment appointment = appointmentService.create(dto);
//        return ResponseEntity.ok(AppointmentResponseDto.fromEntity(appointment));
//    }

    // 사용자 기준 예약 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentResponseDto>> findByUserId(@PathVariable Integer userId) {
        List<AppointmentResponseDto> result = appointmentService.findByUserId(userId).stream()
            .map(AppointmentResponseDto::fromEntity)
            .toList();
        return ResponseEntity.ok(result);
    }

    // 수의사 기준 예약 조회
    @GetMapping("/vet/{vetId}")
    public ResponseEntity<List<AppointmentResponseDto>> findByVetId(@PathVariable Integer vetId) {
        List<AppointmentResponseDto> result = appointmentService.findByVetId(vetId);
        return ResponseEntity.ok(result);
    }


    // 예약 상태 변경
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
        @RequestParam AppointmentStatus status) {
        Appointment updated = appointmentService.updateStatus(id, status);
        return ResponseEntity.ok(AppointmentResponseDto.fromEntity(updated));
    }

    // 예약 취소 (사용자 요청)
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id) {
        Appointment updated = appointmentService.updateStatus(id,
            Appointment.AppointmentStatus.CANCELLED);
        return ResponseEntity.ok(AppointmentResponseDto.fromEntity(updated));
    }

    // 예약 완료 처리 (수의사 요청)
    @PatchMapping("/{id}/complete")
    public ResponseEntity<?> completeAppointment(
        @PathVariable Long id,
        @RequestParam(required = false) String vetOpinion) {

        Appointment updated = appointmentService.completeAppointment(id, vetOpinion);
        return ResponseEntity.ok(AppointmentResponseDto.fromEntity(updated));
    }

}
