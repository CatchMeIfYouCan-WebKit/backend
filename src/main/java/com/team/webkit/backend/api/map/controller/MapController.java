package com.team.webkit.backend.api.map.controller;

import com.team.webkit.backend.api.map.entity.AnimalHospital;
import com.team.webkit.backend.api.map.entity.MissingPost;
import com.team.webkit.backend.api.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    // 실종 / 목격
    @GetMapping("/posts")
    public ResponseEntity<List<MissingPost>> getPosts(@RequestParam String type) {
        return ResponseEntity.ok(mapService.getMissingOrWitnessPosts(type));
    }

    // 품종 + 털색 필터
    @GetMapping("/posts/by-breed")
    public ResponseEntity<List<MissingPost>> getByBreed(@RequestParam String breed) {
        return ResponseEntity.ok(mapService.getByBreed(breed));
    }

    @GetMapping("/posts/by-coat-color")
    public ResponseEntity<List<MissingPost>> getByCoatColor(@RequestParam String coatColor) {
        return ResponseEntity.ok(mapService.getByCoatColor(coatColor));
    }

    // 기존 품종+털색 필터도 유지
    @GetMapping("/posts/filter")
    public ResponseEntity<List<MissingPost>> getByBreedAndColor(
            @RequestParam String breed,
            @RequestParam String coatColor) {
        return ResponseEntity.ok(mapService.getByBreedAndColor(breed, coatColor));
    }

    // 보호소 마커
    @GetMapping("/shelters")
    public ResponseEntity<List<Object[]>> getShelters() {
        return ResponseEntity.ok(mapService.getDistinctShelters());
    }

    // 병원 마커
    @GetMapping("/hospitals")
    public ResponseEntity<List<AnimalHospital>> getHospitals() {
        return ResponseEntity.ok(mapService.getAllHospitals());
    }
}

