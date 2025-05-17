package com.team.webkit.backend.api.adopt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;

import com.team.webkit.backend.api.adopt.dto.AdoptPostRequest;
import com.team.webkit.backend.api.adopt.dto.AdoptPostResponse;
import com.team.webkit.backend.api.adopt.service.AdoptPostService;
import com.team.webkit.backend.support.FileService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = { "http://localhost:5173", "http://10.0.2.2:5173" }) // 앱 & 웹 모두 대응
@RestController
@RequestMapping("/api/adopt")
@RequiredArgsConstructor
public class AdoptPostController {

    private final AdoptPostService adoptPostService;
    private final FileService fileService;
    private final AdoptPostService postService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // ✅ 1. 등록 (POST /api/adopt)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdoptPostResponse> create(@RequestBody AdoptPostRequest request) {
        AdoptPostResponse response = adoptPostService.create(request);
        return ResponseEntity.ok(response);
    }

    // ✅ 2. 전체 조회 (GET /api/adopt)
    @GetMapping
    public ResponseEntity<List<AdoptPostResponse>> getAll() {
        return ResponseEntity.ok(adoptPostService.getAll());
    }

    // ✅ 2-1. 필터링 조회 (GET /api/adopt/filter)
    @GetMapping("/filter")
    public ResponseEntity<List<AdoptPostResponse>> getFiltered(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String color
    ) {
        return ResponseEntity.ok(adoptPostService.getFiltered(region, age, breed, color));
    }

    // ✅ 3. 수정 (PUT /api/adopt/{id})
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdoptPostResponse> update(
            @PathVariable Long id,
            @RequestBody Map<String, Object> requestMap
    ) throws JsonProcessingException {
        AdoptPostRequest request = objectMapper.convertValue(requestMap.get("adopt"), AdoptPostRequest.class);
        List<String> keepImages = objectMapper.convertValue(
                requestMap.getOrDefault("keepImages", List.of()),
                new TypeReference<List<String>>() {}
        );

        AdoptPostResponse response = adoptPostService.update(id, request, keepImages);
        return ResponseEntity.ok(response);
    }

    // ✅ 3-1. 이미지 업로드 전용 엔드포인트 (POST /api/adopt/image-upload)
    @PostMapping(value = "/image-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadAdoptImage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        // adoptUploads 하위 폴더에 저장
        String photoPath = fileService.save(file, "adoptUploads");
        return ResponseEntity.ok(Map.of("photoPath", photoPath));
    }

    // ✅ 4. 삭제 (DELETE /api/adopt/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adoptPostService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** 🔥 status 만 변경하는 엔드포인트 */
    @PatchMapping("/{id}/status")
    public ResponseEntity<AdoptPostResponse> updateStatus(
        @PathVariable Long id,
        @RequestBody Map<String, String> body  // { "status": "분양완료" }
    ) {
        String newStatus = body.get("status");
        AdoptPostResponse updated = postService.updateStatus(id, newStatus);
        return ResponseEntity.ok(updated);
    }
}
