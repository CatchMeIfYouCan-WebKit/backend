package com.team.webkit.backend.api.missing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team.webkit.backend.api.missing.dto.MissingRequest;
import com.team.webkit.backend.api.missing.dto.MissingResponse;
import com.team.webkit.backend.api.missing.dto.WitnessRequest;
import com.team.webkit.backend.api.missing.dto.WitnessResponse;
import com.team.webkit.backend.api.missing.service.MissingService;
import com.team.webkit.backend.api.pet.service.FileService;
import com.team.webkit.backend.support.annotation.MSP;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@MSP
@RestController
@RequestMapping("/api/missing")
@RequiredArgsConstructor
public class MissingController {

    private final MissingService missingService;
    private final FileService fileService;

    // 실종 게시글 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MissingResponse> create(@RequestPart("post") String missingJson,
        @RequestPart(value = "file", required = false)
        MultipartFile file) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        MissingRequest request = mapper.readValue(missingJson, MissingRequest.class);

        if (file != null && !file.isEmpty()) {
            request.photoUrl = fileService.save(file);
        }

        return ResponseEntity.ok(missingService.create(request));
    }

    // 목격 게시글 등록
    @PostMapping(value = "/witness", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WitnessResponse> createWitness(
        @RequestPart("post") String witnessJson,
        @RequestPart(value = "file", required = false) MultipartFile file)
        throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        WitnessRequest request = mapper.readValue(witnessJson, WitnessRequest.class);

        if (file != null && !file.isEmpty()) {
            request.photoUrl = fileService.save(file);
        }

        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        return ResponseEntity.ok(missingService.createWitness(request, userId));
    }


    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<MissingResponse>> getAll() {
        return ResponseEntity.ok(missingService.getAll());
    }

    // 게시글 상세조회
    @GetMapping("/{id}")
    public ResponseEntity<MissingResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(missingService.get(id));
    }

    // 실종 게시글 수정
    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MissingResponse> updateMissing(@PathVariable Long id,
        @RequestPart("post") String missingJson,
        @RequestPart(value = "file", required = false) MultipartFile file)
        throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        MissingRequest request = mapper.readValue(missingJson, MissingRequest.class);

        if (file != null && !file.isEmpty()) {
            request.photoUrl = fileService.save(file);
        }

        return ResponseEntity.ok(missingService.updateMissing(id, request));
    }

    // 목격 게시글 수정
    @PostMapping(value = "/{id}/witness", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WitnessResponse> updateWitness(
        @PathVariable Long id,
        @RequestPart("post") String witnessJson,
        @RequestPart(value = "file", required = false) MultipartFile file
    ) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        WitnessRequest request = mapper.readValue(witnessJson, WitnessRequest.class);

        if (file != null && !file.isEmpty()) {
            request.photoUrl = fileService.save(file);
        }

        return ResponseEntity.ok(missingService.updateWitness(id, request));
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        missingService.delete(id);
        return ResponseEntity.ok(Map.of("message", "삭제 완료"));
    }

    // 게시글 검색 : 사용자
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MissingResponse>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(missingService.getByUser(userId));
    }

    // 게시글 검색 : 펫
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<MissingResponse>> getByPet(@PathVariable Integer petId) {
        return ResponseEntity.ok(missingService.getByPet(petId));
    }

    // 게시글 검색 : 실종 or 목격
    @GetMapping("/type/{type}")
    public ResponseEntity<List<MissingResponse>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(missingService.getByType(type));
    }
}
