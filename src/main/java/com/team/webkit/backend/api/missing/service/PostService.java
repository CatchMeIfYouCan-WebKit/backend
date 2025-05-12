package com.team.webkit.backend.api.missing.service;

import com.team.webkit.backend.api.missing.dto.MissingResponse;
import com.team.webkit.backend.api.missing.entity.Missing;
import com.team.webkit.backend.api.missing.repository.MissingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final MissingRepository missingRepository;

    // 게시글 전체 조회
    public List<MissingResponse> getAll() {
        log.info("전체 게시글 조회 요청");

        return missingRepository.findAll().stream().map(MissingResponse::from).toList();
    }

    // 게시글 상세조회
    public MissingResponse get(Long id) {
        log.info("게시글 단건 조회 요청 (ID: {})", id);

        return MissingResponse.from(missingRepository.findById(id).orElseThrow());
    }

    // 게시글 삭제
    public void delete(Long id) {
        log.info("게시글 삭제 요청 (ID: {})", id);

        Missing post = missingRepository.findById(id).orElseThrow();

        Integer currentUserId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        if (!post.getMember().getId().equals(currentUserId)) {
            log.warn("삭제 권한 없음 - 요청자 ID: {}, 게시글 작성자 ID: {}", currentUserId,
                post.getMember().getId());
            throw new RuntimeException("게시글 작성자만 삭제할 수 있습니다.");
        }

        missingRepository.deleteById(id);
        log.info("게시글 삭제 완료 (ID: {})", id);
    }

    // 게시글 검색 : 사용자
    public List<MissingResponse> getByUser(Integer userId) {
        log.info("게시글 검색 - 사용자 (userId: {})", userId);

        return missingRepository.findByMemberId(userId).stream().map(MissingResponse::from)
            .toList();
    }

    // 게시글 검색 : 펫
    public List<MissingResponse> getByPet(Integer petId) {
        log.info("게시글 검색 - 펫 (petId: {})", petId);
        return missingRepository.findByPetId(petId).stream().map(MissingResponse::from).toList();
    }

    // 게시글 검색 : 실종 or 목격
    public List<MissingResponse> getByType(String type) {
        log.info("게시글 검색 - 실종/목격 (type: {})", type);
        return missingRepository.findByPostType(Missing.PostType.valueOf(type)).stream()
            .map(MissingResponse::from).toList();
    }
}
