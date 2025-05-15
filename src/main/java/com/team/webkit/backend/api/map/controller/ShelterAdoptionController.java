package com.team.webkit.backend.api.map.controller;

import com.team.webkit.backend.api.map.entity.ShelterAdoption;
import com.team.webkit.backend.api.map.service.ShelterAdoptionService;
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
public class ShelterAdoptionController {

    private final ShelterAdoptionService shelterAdoptionService;

    // 보호소(입양) 전체조회
    @GetMapping("shelter-adoption")
    public ResponseEntity<List<ShelterAdoption>> getAllAnimals() {
        return ResponseEntity.ok(shelterAdoptionService.getAllAnimals());
    }

    // 보호소(입양) 상세조회
    @GetMapping("shelter-adoption/{id}")
    public ResponseEntity<?> getAnimal(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(shelterAdoptionService.getAnimal(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 위도, 경도 구하기
    @GetMapping("/shelter-adoption/convert-coords")
    public ResponseEntity<String> convertShelterAdoptionCoords() {
        shelterAdoptionService.convertCoordinates();
        return ResponseEntity.ok("입양 동물 좌표 변환 완료");
    }


}
