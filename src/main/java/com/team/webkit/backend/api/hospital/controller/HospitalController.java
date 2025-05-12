package com.team.webkit.backend.api.hospital.controller;

import com.team.webkit.backend.api.hospital.dto.HospitalResponseDto;
import com.team.webkit.backend.api.hospital.entity.Hospital;
import com.team.webkit.backend.api.hospital.service.HospitalService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    // 전체 병원 조회
    @GetMapping
    public ResponseEntity<?> findAll() {
        List<HospitalResponseDto> result = hospitalService.findAll().stream()
            .map(HospitalResponseDto::fromEntity)
            .toList();

        if (result.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "등록된 병원이 없습니다."));
        }

        return ResponseEntity.ok(result);
    }

    // 병원 상세조회 : 이름, 지역
    @GetMapping("/search")
    public ResponseEntity<?> searchByNameOrAddress(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String address) {

        List<Hospital> hospitals = hospitalService.searchByNameOrAddress(name, address);

        if (hospitals.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "검색 결과가 없습니다."));
        }

        List<HospitalResponseDto> result = hospitals.stream()
            .map(HospitalResponseDto::fromEntity)
            .toList();

        return ResponseEntity.ok(result);
    }


    // 병원 상세조회 : 아이디
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        Optional<Hospital> optionalHospital = hospitalService.findById(id);

        if (optionalHospital.isPresent()) {
            HospitalResponseDto dto = HospitalResponseDto.fromEntity(optionalHospital.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.ok(Map.of("message", "해당 병원을 찾을 수 없습니다."));
        }
    }


}
