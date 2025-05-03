package com.team.webkit.backend.api.map.controller;

import com.team.webkit.backend.api.map.DTO.HospitalResponse;
import com.team.webkit.backend.api.map.DTO.MapPostResponse;
import com.team.webkit.backend.api.map.DTO.ShelterResponse;
import com.team.webkit.backend.api.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    // ✅ 실종 게시물
    @GetMapping("/missing")
    public ResponseEntity<List<MapPostResponse>> getMissingPosts() {
        return ResponseEntity.ok(mapService.getMissingPosts());
    }

    // ✅ 목격 게시물
    @GetMapping("/witness")
    public ResponseEntity<List<MapPostResponse>> getWitnessPosts() {
        return ResponseEntity.ok(mapService.getWitnessPosts());
    }

    // ✅ 실종 + 목격 게시물
    @GetMapping("/posts")
    public ResponseEntity<List<MapPostResponse>> getAllPosts() {
        return ResponseEntity.ok(mapService.getAllPosts());
    }

    // ✅ 보호소 마커 (shelter_animal_announcements + shelter_adoption_animals)
    @GetMapping("/shelters")
    public ResponseEntity<List<ShelterResponse>> getShelterAnnouncements() {
        return ResponseEntity.ok(mapService.getShelterAnnouncements());
    }

    // ✅ 동물병원 마커
    @GetMapping("/hospitals")
    public ResponseEntity<List<HospitalResponse>> getHospitals() {
        return ResponseEntity.ok(mapService.getAnimalHospitals());
    }

    // ✅ 품종 필터 (실종/목격 게시물 + 해당 pet의 breed 값)
    @GetMapping("/posts/by-breed")
    public ResponseEntity<List<MapPostResponse>> getByBreed(@RequestParam String breed) {
        return ResponseEntity.ok(mapService.getByBreed(breed));
    }

    // ✅ 털색 필터 (실종/목격 게시물 + 해당 pet의 coatColor 값)
    @GetMapping("/posts/by-coat-color")
    public ResponseEntity<List<MapPostResponse>> getByCoatColor(@RequestParam String coatColor) {
        return ResponseEntity.ok(mapService.getByCoatColor(coatColor));
    }

    // ✅ 품종 + 털색 동시 필터
    @GetMapping("/posts/filter")
    public ResponseEntity<List<MapPostResponse>> getByBreedAndColor(
            @RequestParam String breed,
            @RequestParam String coatColor) {
        return ResponseEntity.ok(mapService.getByBreedAndColor(breed, coatColor));
    }
}
