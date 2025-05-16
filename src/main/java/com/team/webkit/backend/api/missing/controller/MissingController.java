package com.team.webkit.backend.api.missing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team.webkit.backend.api.missing.dto.MissingRequest;
import com.team.webkit.backend.api.missing.dto.MissingResponse;
import com.team.webkit.backend.api.missing.service.MissingService;
import com.team.webkit.backend.support.FileService;
import com.team.webkit.backend.support.annotation.MSP;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/posts/missing")
@RequiredArgsConstructor
public class MissingController {

    private final MissingService missingService;
    private final FileService fileService;

    // 실종 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createMissing(
        @RequestPart("post") String missingJson,
        @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        MissingRequest request = mapper.readValue(missingJson, MissingRequest.class);

        if (file != null && !file.isEmpty()) {
            request.photoUrl = fileService.save(file);
        }

        MissingResponse response = missingService.createMissing(request);

        // AI 예측을 위한 최소 응답 정보 반환
        Map<String, Object> result = new HashMap<>();
        result.put("postId", response.id);
        result.put("photoUrl", response.photoUrl);

        return ResponseEntity.ok(result);
    }

    // 실종 전체조회
    @GetMapping("all")
    public ResponseEntity<List<MissingResponse>> getAllMissingPosts() {
        List<MissingResponse> result = missingService.getAllMissingPosts();
        return ResponseEntity.ok(result);
    }

    // 실종 상세조회
    @GetMapping("/{id}")
    public ResponseEntity<MissingResponse> getMissingPost(@PathVariable Long id) {
        return ResponseEntity.ok(missingService.getMissingPostById(id));
    }

    // 실종 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMissingPost(@PathVariable Long id) {
        missingService.deleteMissingPost(id);
        return ResponseEntity.noContent().build();
    }

    // 실종 필터링(품종, 털색)
    @GetMapping
    public ResponseEntity<List<MissingResponse>> getMissingPostsByFilter(
        @RequestParam(required = false) String breed,
        @RequestParam(required = false) String coatColor) {
        List<MissingResponse> result = missingService.findMissingPostsByFilter(breed, coatColor);
        return ResponseEntity.ok(result);
    }
}
