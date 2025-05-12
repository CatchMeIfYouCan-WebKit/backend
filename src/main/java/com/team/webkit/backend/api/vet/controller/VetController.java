package com.team.webkit.backend.api.vet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.webkit.backend.api.vet.dto.VetRequestDto;
import com.team.webkit.backend.api.vet.dto.VetResponseDto;
import com.team.webkit.backend.api.vet.entity.Vet;
import com.team.webkit.backend.api.vet.service.VetService;
import com.team.webkit.backend.support.FileService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/vet")
@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;
    private final FileService fileService;

    // 회원가입
    @PostMapping(
        value = "/join",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> join(
        @RequestPart("vet") String vetJson,
        @RequestPart(value = "file", required = false) MultipartFile file
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        VetRequestDto dto = objectMapper.readValue(vetJson, VetRequestDto.class);

        if (vetService.existsByLoginId(dto.getLoginId())) {
            return ResponseEntity.badRequest().body(Map.of("message", "이미 사용중인 아이디입니다."));
        }

        if (file != null && !file.isEmpty()) {
            String photoPath = fileService.save(file);
            dto.setLicenseImageUrl(photoPath);
        }

        Vet vet = vetService.join(dto);
        return ResponseEntity.ok(VetResponseDto.fromEntity(vet));
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

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody VetRequestDto dto) {
        Optional<Vet> vet = vetService.login(dto.getLoginId(), dto.getPassword());

        if (vet.isPresent()) {
            return ResponseEntity.ok(Map.of("message", "로그인 성공"));
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "아이디 또는 비밀번호가 올바르지 않습니다."));
        }
    }

    // 모든 수의사 조회
    @GetMapping
    public ResponseEntity<List<VetResponseDto>> findAll() {
        List<VetResponseDto> vets = vetService.findAll()
            .stream()
            .map(VetResponseDto::fromEntity)
            .toList();

        return ResponseEntity.ok(vets);
    }

    // 수의사 상세조회 : 병원
    @GetMapping("/search")
    public ResponseEntity<?> findByHospitalId(@RequestParam Integer hospitalId) {
        List<Vet> vets = vetService.findByHospitalId(hospitalId);

        if (vets.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "해당 병원에 등록된 수의사가 없습니다."));
        }

        List<VetResponseDto> result = vets.stream()
            .map(VetResponseDto::fromEntity)
            .toList();

        return ResponseEntity.ok(result);
    }

    // 수의사 상세조회 : 아이디
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        Optional<Vet> vet = vetService.findById(id);

        if (vet.isPresent()) {
            return ResponseEntity.ok(VetResponseDto.fromEntity(vet.get()));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "해당 수의사를 찾을 수 없습니다."));
        }
    }


}