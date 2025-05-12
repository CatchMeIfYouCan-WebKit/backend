package com.team.webkit.backend.api.vet.service;

import com.team.webkit.backend.api.vet.dto.VetRequestDto;
import com.team.webkit.backend.api.vet.entity.Vet;
import com.team.webkit.backend.api.vet.repository.VetRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VetService {

    private final VetRepository vetRepository;

    // 회원가입
    public Vet join(VetRequestDto dto) {
        log.info("회원가입 요청: loginId={}, name={}", dto.getLoginId(), dto.getName());

        if (vetRepository.existsByLoginId(dto.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        Vet vet = dto.toEntity();
        Vet savedVet = vetRepository.save(vet);

        log.info("회원가입 완료: id={}, loginId={}", savedVet.getId(), savedVet.getLoginId());

        return savedVet;
    }

    // 아이디 중복 체크
    public boolean existsByLoginId(String loginId) {
        return vetRepository.existsByLoginId(loginId);
    }

    // 로그인
    public Optional<Vet> login(String loginId, String password) {
        log.info("로그인 시도: loginId={}", loginId);

        if (!vetRepository.existsByLoginId(loginId)) {
            log.warn("로그인 실패 - 존재하지 않는 아이디: loginId={}", loginId);
            return Optional.empty();
        }

        Vet vet = vetRepository.findByLoginId(loginId).get();

        if (vet.getPassword().equals(password)) {
            log.info("로그인 성공: loginId={}", loginId);
            return Optional.of(vet);
        } else {
            log.warn("로그인 실패 - 비밀번호 불일치: loginId={}", loginId);
            return Optional.empty();
        }
    }

    // 수의사 조회 : 아이디
    public Optional<Vet> findById(Integer id) {
        log.info("수의사 정보 조회: id={}", id);
        return vetRepository.findById(id);
    }

    // 모든 수의사 조회
    public List<Vet> findAll() {
        log.info("모든 수의사 정보 조회");
        return vetRepository.findAll();
    }


    // 수의사 조회 : 병원
    public List<Vet> findByHospitalId(Integer hospitalId) {
        return vetRepository.findByHospitalId(hospitalId);
    }

}
