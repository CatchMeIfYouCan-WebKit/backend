package com.team.webkit.backend.api.map.controller;

import com.team.webkit.backend.api.map.entity.ShelterMissing;
import com.team.webkit.backend.api.map.service.ShelterMissingService;
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
public class ShelterMissingController {


    private final ShelterMissingService shelterMissingService;

    // 보호소(실종) 전체조회
    @GetMapping("shelter-missing")
    public ResponseEntity<List<ShelterMissing>> getAllMissingAnimals() {
        return ResponseEntity.ok(shelterMissingService.getAllMissingAnimals());
    }

    // 보호소(실종) 상세조회
    @GetMapping("/shelter-missing/{id}")
    public ResponseEntity<?> getMissingAnimal(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(shelterMissingService.getMissingAnimal(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 위도, 경도 구하기
    @GetMapping("/shelter-missing/convert-coords")
    public ResponseEntity<String> convertShelterMissingCoords() {
        shelterMissingService.convertCoordinates();
        return ResponseEntity.ok("실종 동물 좌표 변환 완료");
    }

}
