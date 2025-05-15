package com.team.webkit.backend.api.vet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.webkit.backend.api.vet.dto.VetRequestDto;
import com.team.webkit.backend.api.vet.dto.VetResponseDto;
import com.team.webkit.backend.api.vet.entity.Vet;
import com.team.webkit.backend.api.vet.service.VetService;
import com.team.webkit.backend.config.JwtUtil;
import com.team.webkit.backend.support.FileService;
import java.util.LinkedHashMap;
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
    private final JwtUtil jwtUtil;

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

    // 아이디 중복 검사
    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Boolean>> checkDuplicateId(@RequestParam String loginId) {
        boolean exists = vetService.existsByLoginId(loginId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }


    // 이미지 업로드
    @CrossOrigin(origins = "http://10.0.2.2:5174")
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
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");
        String password = request.get("password");

        Map<String, String> body = new LinkedHashMap<>();

        if (loginId == null || loginId.isEmpty() || password == null || password.isEmpty()) {
            body.put("rsltMsg", "필수값 누락");
        } else {
            Optional<Vet> optionalVet = vetService.login(loginId, password);

            if (optionalVet.isPresent()) {
                Vet vet = optionalVet.get();
                String token = jwtUtil.createAccessToken(vet);

                body.put("rsltMsg", "로그인 성공");
                body.put("accessToken", token);
                body.put("vetId", vet.getId().toString()); // ✅ vetId도 반환
            } else {
                body.put("rsltMsg", "아이디 혹은 비밀번호가 맞지 않습니다.");
            }
        }

        return ResponseEntity.ok(body);

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