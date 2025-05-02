package com.team.webkit.backend.api.map.service;

import com.team.webkit.backend.api.map.Repository.*;
import com.team.webkit.backend.api.map.entity.AnimalHospital;
import com.team.webkit.backend.api.map.entity.MissingPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {

    private final MissingPostRepository missingPostRepository;
    private final ShelterAnimalAnnouncementRepository shelterAnnouncementRepository;
    private final AnimalHospitalRepository animalHospitalRepository;

    // 실종 or 목격 마커
    public List<MissingPost> getMissingOrWitnessPosts(String type) {
        if (type.equalsIgnoreCase("all")) {
            return missingPostRepository.findAll(); // 모든 게시물 반환
        }
        return missingPostRepository.findByPostType(MissingPost.PostType.valueOf(type));
    }


    public List<MissingPost> getByBreed(String breed) {
        return missingPostRepository.findByBreed(breed);
    }

    public List<MissingPost> getByCoatColor(String coatColor) {
        return missingPostRepository.findByCoatColor(coatColor);
    }

    public List<MissingPost> getByBreedAndColor(String breed, String coatColor) {
        return missingPostRepository.findByBreedAndCoatColor(breed, coatColor);
    }

    // 보호소 마커
    public List<Object[]> getDistinctShelters() {
        return shelterAnnouncementRepository.findDistinctShelters();
    }

    // 병원 마커
    public List<AnimalHospital> getAllHospitals() {
        return animalHospitalRepository.findAll();
    }
}

