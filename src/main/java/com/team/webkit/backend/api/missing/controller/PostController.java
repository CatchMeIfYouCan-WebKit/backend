package com.team.webkit.backend.api.missing.controller;

import com.team.webkit.backend.api.missing.dto.MissingResponse;
import com.team.webkit.backend.api.missing.service.PostService;
import com.team.webkit.backend.support.annotation.MSP;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@MSP
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<MissingResponse>> getAll() {
        return ResponseEntity.ok(postService.getAll());
    }


    // 게시글 상세조회
    @GetMapping("/{id}")
    public ResponseEntity<MissingResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(postService.get(id));
    }


    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok(Map.of("message", "삭제 완료"));
    }


    // 게시글 검색 : 사용자
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MissingResponse>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(postService.getByUser(userId));
    }


    // 게시글 검색 : 펫
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<MissingResponse>> getByPet(@PathVariable Integer petId) {
        return ResponseEntity.ok(postService.getByPet(petId));
    }


    // 게시글 검색 : 실종 or 목격
    @GetMapping("/type/{type}")
    public ResponseEntity<List<MissingResponse>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(postService.getByType(type));
    }
}
