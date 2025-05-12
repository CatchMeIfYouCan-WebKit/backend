package com.team.webkit.backend.api.missing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team.webkit.backend.api.missing.dto.MissingRequest;
import com.team.webkit.backend.api.missing.dto.MissingResponse;
import com.team.webkit.backend.api.missing.service.MissingService;
import com.team.webkit.backend.support.FileService;
import com.team.webkit.backend.support.annotation.MSP;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@MSP
@RestController
@RequestMapping("/api/posts/missing")
@RequiredArgsConstructor
public class MissingController {


    private final MissingService missingService;
    private final FileService fileService;


    // 실종 게시글 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MissingResponse> createMissing(@RequestPart("post") String missingJson,
        @RequestPart(value = "file", required = false)
        MultipartFile file) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        MissingRequest request = mapper.readValue(missingJson, MissingRequest.class);

        if (file != null && !file.isEmpty()) {
            request.photoUrl = fileService.save(file);
        }

        return ResponseEntity.ok(missingService.createMissing(request));
    }


//    // 실종 게시글 수정
//    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<MissingResponse> updateMissing(@PathVariable Long id,
//        @RequestPart("post") String missingJson,
//        @RequestPart(value = "file", required = false) MultipartFile file)
//        throws JsonProcessingException {
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        MissingRequest request = mapper.readValue(missingJson, MissingRequest.class);
//
//        if (file != null && !file.isEmpty()) {
//            request.photoUrl = fileService.save(file);
//        }
//
//        return ResponseEntity.ok(missingService.updateMissing(id, request));
//    }
    //지도 불러오기 추가(예찬)
    @GetMapping("/missing-posts")
    public ResponseEntity<List<MissingResponse>> getMissingPosts() {
        return ResponseEntity.ok(missingService.getAllMissingPosts());
    }


}
