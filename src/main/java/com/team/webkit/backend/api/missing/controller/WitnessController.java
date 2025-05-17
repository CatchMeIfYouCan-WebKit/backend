package com.team.webkit.backend.api.missing.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team.webkit.backend.api.missing.dto.MissingResponse;
import com.team.webkit.backend.api.missing.dto.WitnessRequest;
import com.team.webkit.backend.api.missing.dto.WitnessResponse;
import com.team.webkit.backend.api.missing.service.WitnessService;
import com.team.webkit.backend.support.FileService;
import com.team.webkit.backend.support.annotation.MSP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@MSP
@RestController
@RequestMapping("/api/posts/witness")
@RequiredArgsConstructor
public class WitnessController {

    private final WitnessService witnessService;
    private final FileService fileService;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String AI_PREDICT_URL = "http://localhost:8081/ai/predict-from-path";

    // 목격 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createWitness(
            @RequestPart("post") String witnessJson,
            @RequestPart(value = "photoUrls", required = false) String photoUrlsJson // 🔄 경로만 받음
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        WitnessRequest request = mapper.readValue(witnessJson, WitnessRequest.class);

        String representativePhoto = null;
        List<String> paths = new ArrayList<>();

        if (photoUrlsJson != null && !photoUrlsJson.isEmpty()) {
            // 🔄 JSON 배열 형태로 넘어온 문자열을 리스트로 변환
            paths = mapper.readValue(photoUrlsJson, new TypeReference<List<String>>() {});
            request.setPhotoUrls(paths);
            representativePhoto = paths.get(0); // 첫 번째 이미지를 대표로 설정
        }

        WitnessResponse response = witnessService.createWitness(request);

        // ✅ AI 예측 호출
        if (representativePhoto != null) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                Map<String, Object> aiReq = new HashMap<>();
                aiReq.put("photo_url", representativePhoto);
                aiReq.put("pet_id", null);
                aiReq.put("post_type", "witness");
                aiReq.put("post_id", response.id);
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(aiReq, headers);
                restTemplate.postForEntity(AI_PREDICT_URL, entity, String.class);
            } catch (Exception e) {
                // 예측 실패해도 무시
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        result.put("postId", response.id);
        result.put("photoUrl", representativePhoto);

        return ResponseEntity.ok(result);
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

    // 목격 전체조회
    @GetMapping("/all")
    public ResponseEntity<List<WitnessResponse>> getAll() {
        return ResponseEntity.ok(witnessService.getAll());
    }

    // 목격 상세조회
    @GetMapping("/{id}")
    public ResponseEntity<WitnessResponse> getWitnessPost(@PathVariable Long id) {
        return ResponseEntity.ok(witnessService.get(id));
    }

    // 목격 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            witnessService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 추천 실종글 조회 (AI 기반)
    @GetMapping("/{id}/recommendations")
    public ResponseEntity<List<WitnessResponse>> getRecommendedMissingPosts(@PathVariable Long id) {
        List<WitnessResponse> recommended = witnessService.getRecommendedMissingPosts(id);
        return ResponseEntity.ok(recommended);
    }


}
