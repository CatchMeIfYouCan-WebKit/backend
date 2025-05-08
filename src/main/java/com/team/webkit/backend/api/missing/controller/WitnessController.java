package com.team.webkit.backend.api.missing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team.webkit.backend.api.missing.dto.WitnessRequest;
import com.team.webkit.backend.api.missing.dto.WitnessResponse;
import com.team.webkit.backend.api.missing.service.WitnessService;
import com.team.webkit.backend.api.pet.service.FileService;
import com.team.webkit.backend.support.annotation.MSP;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@MSP
@RestController
@RequestMapping("/api/posts/witness")
@RequiredArgsConstructor
public class WitnessController {

    private final WitnessService witnessService;
    private final FileService fileService;

    // 목격 게시글 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
        return ResponseEntity.ok(witnessService.createWitness(request, userId));
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

        return ResponseEntity.ok(witnessService.updateWitness(id, request));
    }
}
