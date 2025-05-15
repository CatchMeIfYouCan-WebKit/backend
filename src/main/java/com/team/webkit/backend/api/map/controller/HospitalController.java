package com.team.webkit.backend.api.map.controller;

import com.team.webkit.backend.api.map.entity.Hospital;
import com.team.webkit.backend.api.map.service.HospitalService;
import com.team.webkit.backend.support.annotation.MSP;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@MSP
@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    // 병원 전체조회
    @GetMapping("/hospital")
    public List<Hospital> getAllHospitals() {
        return hospitalService.getAllHospitals();
    }

    // 병원 상세조회
    @GetMapping("/hospital/{id}")
    public ResponseEntity<?> getHospital(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(hospitalService.getHospital(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 위도, 경도 구하기
    @GetMapping("/hospital/convert-coords")
    public ResponseEntity<String> convertHospitalCoords() {
        hospitalService.convertCoordinates();
        return ResponseEntity.ok("좌표 변환 완료");
    }
}
