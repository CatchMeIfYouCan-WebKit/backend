package com.team.webkit.backend.api.missing.service;

import com.team.webkit.backend.api.comment.repository.CommentRepository;
import com.team.webkit.backend.api.member.repository.MemberRepository;
import com.team.webkit.backend.api.missing.dto.WitnessRequest;
import com.team.webkit.backend.api.missing.dto.WitnessResponse;
import com.team.webkit.backend.api.missing.entity.Missing;
import com.team.webkit.backend.api.missing.entity.Missing.PostType;
import com.team.webkit.backend.api.missing.repository.MissingRepository;
import com.team.webkit.backend.api.pet.repository.PetRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WitnessService {

    private final MissingRepository missingRepository;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final CommentRepository commentRepository;

    // 목격 등록
    public WitnessResponse createWitness(WitnessRequest req) {
        log.info("목격 게시글 등록 요청: {}", req);

        Integer currentUserId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        var member = memberRepository.findById(currentUserId)
            .orElseThrow(() -> {
                log.error("사용자 없음 - ID: {}", currentUserId);
                return new RuntimeException("사용자를 찾을 수 없습니다.");
            });

        Missing post = new Missing();
        post.setMember(member);
        post.setPostType(Missing.PostType.witness);
        // ✅ URL 목록을 ,로 연결해서 하나의 컬럼에 저장
        if (req.getPhotoUrls() != null && !req.getPhotoUrls().isEmpty()) {
            post.setPhotoUrl(String.join(",", req.getPhotoUrls()));
        }
        post.setMissingDatetime(req.witnessDatetime);
        post.setMissingLocation(req.witnessLocation);
        post.setDetailDescription(req.detailDescription);

        Missing saved = missingRepository.save(post);
        log.info("목격 게시글 등록 완료 (ID: {})", saved.getId());
        log.info("request.photoUrl: " + req.photoUrls);

        return WitnessResponse.from(saved);
    }


    // 목격 전체조회
    public List<WitnessResponse> getAll() {
        List<Missing> posts = missingRepository.findByPostType(PostType.witness);

        return posts.stream()
            .map(post -> {
                WitnessResponse res = WitnessResponse.from(post);
                res.commentCount = commentRepository.countByPost_Id(post.getId());
                return res;
            })
            .collect(Collectors.toList());
    }


    // 게시글 상세조회
    public WitnessResponse get(Long id) {
        log.info("목격 게시글 단건 조회 서비스 호출 (ID: {})", id);
        return WitnessResponse.from(
            missingRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("조회 실패: 존재하지 않는 게시글 ID={}", id);
                    return new RuntimeException("게시글을 찾을 수 없습니다.");
                })
        );
    }

    // 목격 삭제
    public void delete(Long id) {
        Missing post = missingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!"witness".equalsIgnoreCase(String.valueOf(post.getPostType()))) {
            log.warn("삭제 실패: 게시글 타입이 witness가 아님 (ID: {}, postType: {})", id, post.getPostType());
            throw new RuntimeException("목격 게시글만 삭제할 수 있습니다.");
        }

        missingRepository.delete(post);
        log.info("목격 게시글 삭제 성공 (ID: {})", id);
    }


}

