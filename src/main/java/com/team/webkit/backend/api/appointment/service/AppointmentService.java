package com.team.webkit.backend.api.appointment.service;

import com.team.webkit.backend.api.appointment.dto.AppointmentResponseDto;
import com.team.webkit.backend.api.appointment.entity.Appointment;
import com.team.webkit.backend.api.appointment.entity.Appointment.AppointmentStatus;
import com.team.webkit.backend.api.appointment.repository.AppointmentRepository;
import com.team.webkit.backend.api.member.repository.MemberRepository;
import com.team.webkit.backend.api.pet.repository.PetRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    // Todo 나중에 다시 연관관계, requestDto, responseDto 다른 파일 참고해서.
    // 예약 신청
//    public Appointment create(AppointmentRequestDto dto) {
//        log.info("진료 예약 생성 요청: userId={}, vetId={}, petId={}, appointmentTime={}",
//            // User, Pet 엔티티 조회
//            Member user = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
//
//        Pet pet = petRepository.findById(dto.getPetId())
//            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 반려동물입니다."));
//
//        int pastVisits = appointmentRepository.countByUserIdAndVetId(dto.getUserId(), dto.getVetId());
//
//        // Appointment 엔티티 생성 (연관관계 포함)
//        Appointment appointment = dto.toEntity(user, pet, pastVisits + 1);
//
//        // 저장
//        return appointmentRepository.save(appointment);
//    }

    // 사용자 기준 예약 조회
    public List<Appointment> findByUserId(Integer userId) {
        log.info("사용자 예약 조회 요청: userId={}", userId);
        List<Appointment> result = appointmentRepository.findByUserId(userId);
        log.info("조회 결과: {}건", result.size());
        return result;
    }

    // 수의사 기준 예약 조회
    public List<AppointmentResponseDto> findByVetId(Integer vetId) {
        List<Appointment> appointments = appointmentRepository.findByVetId(vetId);

        return appointments.stream()
            .map(AppointmentResponseDto::fromEntity)
            .collect(Collectors.toList());
    }


    public Optional<Appointment> findById(Long id) {
        log.info("예약 상세 조회 요청: appointmentId={}", id);
        return appointmentRepository.findById(id);
    }

    // 예약 상태 변경
    public Appointment updateStatus(Long id, AppointmentStatus status) {
        log.info("예약 상태 변경 요청: appointmentId={}, status={}", id, status);

        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        appointment.setStatus(status);
        Appointment updated = appointmentRepository.save(appointment);

        log.info("예약 상태 변경 완료: appointmentId={}, newStatus={}", id, updated.getStatus());
        return updated;
    }

    // 예약 완료
    public Appointment completeAppointment(Long id, String vetOpinion) {
        log.info("예약 완료 처리 요청: appointmentId={}, vetOpinion={}", id, vetOpinion);

        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        if (vetOpinion != null && !vetOpinion.isBlank()) {
            appointment.setVetOpinion(vetOpinion);
        }

        Appointment updated = appointmentRepository.save(appointment);
        log.info("예약 완료 처리 완료: appointmentId={}, status={}", id, updated.getStatus());
        return updated;
    }

}
