package com.team.webkit.backend.api.pet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team.webkit.backend.api.pet.dto.PetResponseDto;
import com.team.webkit.backend.api.pet.entity.Pet;
import com.team.webkit.backend.api.pet.service.FileService;
import com.team.webkit.backend.api.pet.service.PetService;
import com.team.webkit.backend.support.annotation.MSP;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@MSP
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/animal-profile")
public class PetController {

    private final PetService petService;
    private final FileService fileService;

    // 반려동물 등록
    @PostMapping(
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> add(@RequestPart("pet") String petJson,
        @RequestPart("file") MultipartFile file) throws JsonProcessingException {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        System.out.println("[파일 수신 여부] file == null: " + (file == null));
        System.out.println("[파일 수신 여부] file.isEmpty(): " + (file != null && file.isEmpty()));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Pet pet = objectMapper.readValue(petJson, Pet.class);

        if (file != null && !file.isEmpty()) {
            String photoPath = fileService.save(file);
            pet.setPhotoPath(photoPath);
        }

        return ResponseEntity.ok(petService.add(pet, userId));
    }


    // 이미지 업로드
    @CrossOrigin(origins = "http://10.0.2.2:5173")
    @PostMapping(value = "/image-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        String photoPath = fileService.save(file);
        return ResponseEntity.ok(Map.of("photoPath", photoPath));
    }


    // 내 반려동물 전체 조회
    @GetMapping("/all")
    public ResponseEntity<List<Pet>> findAll() {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        return ResponseEntity.ok(petService.findAll(userId));
    }

    // 내 반려동물 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Pet> findById(@PathVariable Integer id) {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        return ResponseEntity.ok(petService.findById(id, userId));
    }

    // 반려동물 수정
    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@PathVariable Integer id,
        @RequestPart("pet") String petJson,
        @RequestPart(value = "file", required = false) MultipartFile file)
        throws JsonProcessingException {

        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Pet pet = objectMapper.readValue(petJson, Pet.class);

        if (file != null && !file.isEmpty()) {
            String photoPath = fileService.save(file);
            pet.setPhotoPath(photoPath);
        } else if (pet.getPhotoPath() == null) {
            // 파일도 없고 photoPath도 없을 경우 기존 값 유지
            Pet original = petService.findById(id, userId);
            pet.setPhotoPath(original.getPhotoPath());
        }

        return ResponseEntity.ok(petService.update(id, pet, userId));
    }


    // 반려동물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        petService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    // 반려동물의 주인 조회
    @GetMapping("/{id}/owner")
    public ResponseEntity<PetResponseDto> getPetOwner(@PathVariable Integer id) {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        return ResponseEntity.ok(petService.getOwner(id, userId));
    }

    // 동물등록번호 중복 검사
    @GetMapping("/checkRegistrationNo")
    public ResponseEntity<Map<String, Object>> checkRegistrationNo(
        @RequestParam String registrationNo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal()
            .equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "로그인이 필요합니다."));
        }

        Integer userId = (Integer) auth.getPrincipal();
        boolean exists = petService.checkRegNum(registrationNo, userId);

        return ResponseEntity.ok(Map.of("exists", exists));
    }


}
