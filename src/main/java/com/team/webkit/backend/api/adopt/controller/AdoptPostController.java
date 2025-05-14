package com.team.webkit.backend.api.adopt.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team.webkit.backend.api.adopt.dto.AdoptPostRequest;
import com.team.webkit.backend.api.adopt.dto.AdoptPostResponse;
import com.team.webkit.backend.api.adopt.service.AdoptPostService;
import com.team.webkit.backend.support.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/adopt")
@RequiredArgsConstructor
public class AdoptPostController {

    private final AdoptPostService adoptPostService;
    private final FileService fileService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    //수정
    // ✅ 1. 등록 (POST /api/adopt)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdoptPostResponse> create(
            @RequestPart("adopt") String adoptPostJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws JsonProcessingException {
        AdoptPostRequest request = objectMapper.readValue(adoptPostJson, AdoptPostRequest.class);

        // 이미지 여러 개를 서비스로 전달해서 처리
        AdoptPostResponse response = adoptPostService.create(request, files);
        return ResponseEntity.ok(response);
    }


    // ✅ 2. 전체 조회 (GET /api/adopt)
    @GetMapping
    public ResponseEntity<List<AdoptPostResponse>> getAll() {
        return ResponseEntity.ok(adoptPostService.getAll());
    }
    // 필터링 조회
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
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdoptPostResponse> update(
            @PathVariable Long id,
            @RequestPart("adopt") String adoptPostJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "keepImages", required = false) String keepImagesJson  // 👈 추가
    ) throws JsonProcessingException {
        AdoptPostRequest request = objectMapper.readValue(adoptPostJson, AdoptPostRequest.class);
        List<String> keepImages = objectMapper.readValue(keepImagesJson, new TypeReference<List<String>>() {}); // 👈 JSON 문자열 파싱

        AdoptPostResponse response = adoptPostService.update(id, request, files, keepImages); // 👈 서비스에서도 받아서 반영
        return ResponseEntity.ok(response);
    }




    // ✅ 4. 삭제 (DELETE /api/adopt/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adoptPostService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
