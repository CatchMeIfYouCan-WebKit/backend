package com.team.webkit.backend.api.map.service;

import com.team.webkit.backend.api.map.DTO.HospitalResponse;
import com.team.webkit.backend.api.map.DTO.MapPostResponse;
import com.team.webkit.backend.api.map.DTO.ShelterResponse;
import com.team.webkit.backend.api.map.Repository.AnimalHospitalRepository;
import com.team.webkit.backend.api.map.Repository.ShelterAdoptionAnimalRepository;
import com.team.webkit.backend.api.map.Repository.ShelterAnimalAnnouncementRepository;
import com.team.webkit.backend.api.missing.Missing;
import com.team.webkit.backend.api.map.Repository.MissingPostRepository;
import com.team.webkit.backend.api.missing.PostType;
import com.team.webkit.backend.api.pet.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapService {

    private final MissingPostRepository missingPostRepository;
    private final ShelterAnimalAnnouncementRepository shelterAnimalAnnouncementRepository;
    private final AnimalHospitalRepository animalHospitalRepository;
    private final ShelterAdoptionAnimalRepository shelterAdoptionAnimalRepository;



    // 1. 실종 + 목격
    public List<MapPostResponse> getAllPosts() {
        List<Missing> posts = missingPostRepository.findAllWithPet();
        return convertToResponse(posts);
    }

    // 2. 실종만
    public List<MapPostResponse> getMissingPosts() {
        List<Missing> posts = missingPostRepository.findByPostTypeWithPet(PostType.missing);
        return convertToResponse(posts);
    }

    // 3. 목격만
    public List<MapPostResponse> getWitnessPosts() {
        List<Missing> posts = missingPostRepository.findByPostTypeWithPet(PostType.witness);
        return convertToResponse(posts);
    }

    // 4. 품종
    public List<MapPostResponse> getByBreed(String breed) {
        List<Missing> posts = missingPostRepository.findByPetBreed(breed);
        return convertToResponse(posts);
    }

    // 5. 털색
    public List<MapPostResponse> getByCoatColor(String coatColor) {
        List<Missing> posts = missingPostRepository.findByPetCoatColor(coatColor);
        return convertToResponse(posts);
    }

    // 6. 품종 + 털색
    public List<MapPostResponse> getByBreedAndColor(String breed, String coatColor) {
        List<Missing> posts = missingPostRepository.findByPetBreedAndCoatColor(breed, coatColor);
        return convertToResponse(posts);
    }

    // 🔄 공통 변환 로직
    private List<MapPostResponse> convertToResponse(List<Missing> posts) {
        return posts.stream().map(post -> {
            Pet pet = post.getPet();
            return MapPostResponse.builder()
                    .postType(post.getPostType().name())
                    .missingLocation(post.getMissingLocation())
                    .photoUrl(post.getPhotoUrl())
                    .missingDatetime(post.getMissingDatetime())
                    .breed(pet.getBreed())
                    .coatColor(pet.getCoatColor())
                    .build();
        }).collect(Collectors.toList());
    }
    public List<ShelterResponse> getShelterAnnouncements() {
        List<ShelterResponse> fromAnnouncements = shelterAnimalAnnouncementRepository.findAll().stream()
                .map(announcement -> ShelterResponse.builder()
                        .shelterName(announcement.getShelterName())
                        .phone(announcement.getPhone())
                        .address(announcement.getAddress()) // ✅ address 포함
                        .build())
                .collect(Collectors.toList());

        List<ShelterResponse> fromAdoptions = shelterAdoptionAnimalRepository.findAll().stream()
                .map(adoption -> ShelterResponse.builder()
                        .shelterName(adoption.getShelterName())
                        .phone(adoption.getShelterContact())
                        .address(adoption.getProtectionLocation()) // ✅ 보호장소 포함
                        .build())
                .collect(Collectors.toList());

        fromAnnouncements.addAll(fromAdoptions);
        return fromAnnouncements;
    }



    public List<HospitalResponse> getAnimalHospitals() {
        return animalHospitalRepository.findAll().stream()
                .map(hospital -> HospitalResponse.builder()
                        .name(hospital.getName())
                        .phone(hospital.getPhone())
                        .address(hospital.getAddress())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
