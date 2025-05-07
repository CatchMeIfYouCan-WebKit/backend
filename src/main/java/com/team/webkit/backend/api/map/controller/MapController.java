package com.team.webkit.backend.api.map.controller;

import com.team.webkit.backend.api.map.DTO.HospitalResponse;
import com.team.webkit.backend.api.map.DTO.ShelterResponse;
import com.team.webkit.backend.api.map.service.MapService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

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
}
