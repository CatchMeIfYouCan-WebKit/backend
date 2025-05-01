package com.team.webkit.backend.api.pet;

import com.team.webkit.backend.support.annotation.MSP;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@MSP
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pet")
public class PetController {

    private final PetService petService;

    // 반려동물 등록
    @PostMapping
    public ResponseEntity<Pet> add(@RequestBody Pet pet) {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        return ResponseEntity.ok(petService.add(pet, userId));
    }

    // 내 반려동물 전체 조회
    @GetMapping
    public ResponseEntity<List<Pet>> findAll() {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        return ResponseEntity.ok(petService.findAll(userId));
    }

    // 내 반려동물 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Pet> findById(@PathVariable Integer id) {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        return ResponseEntity.ok(petService.findById(id, userId));
    }

    // 반려동물 수정
    @PutMapping("/{id}")
    public ResponseEntity<Pet> update(@PathVariable Integer id, @RequestBody Pet request) {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        return ResponseEntity.ok(petService.update(id, request, userId));
    }

    // 반려동물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        petService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

}
