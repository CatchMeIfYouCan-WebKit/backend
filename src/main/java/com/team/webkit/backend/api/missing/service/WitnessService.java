package com.team.webkit.backend.api.missing.service;

import com.team.webkit.backend.api.member.repository.MemberRepository;
import com.team.webkit.backend.api.missing.dto.WitnessRequest;
import com.team.webkit.backend.api.missing.dto.WitnessResponse;
import com.team.webkit.backend.api.missing.entity.Missing;
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

    // 목격 게시글 등록
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

    // 목격 게시글 상세조회
    public WitnessResponse getWitnessPostById(Long id) {
        log.info("목격 게시글 상세 조회 요청: 게시글 ID={}", id);

        return missingRepository.findById(id)
            .map(post -> {
                WitnessResponse response = WitnessResponse.from(post);
                log.info(
                    "게시글 조회 성공: [ID: {}], [작성자 ID: {}], [PostType: {}], [UserID: {}], [PhotoURL: {}], "
                        +
                        "[WitnessDatetime: {}], [WitnessLocation: {}], [DetailDescription: {}], [Address: {}], "
                        +
                        "[Breed: {}], [CoatColor: {}], [CreatedAt: {}], [UpdatedAt: {}], [UserNickname: {}], [UserPhone: {}]",
                    response.id,
                    response.userId,
                    response.postType,
                    response.userId,
                    response.photoUrl,
                    response.witnessDatetime,
                    response.witnessLocation,
                    response.detailDescription,
                    response.address,
                    response.breed,
                    response.coatColor,
                    response.createdAt,
                    response.updatedAt,
                    response.userNickname,
                    response.userPhone
                );
                return response;
            })
            .orElseThrow(() -> {
                log.error("게시글 조회 실패: 존재하지 않는 게시글 ID={}", id);
                return new RuntimeException("게시글을 찾을 수 없습니다.");
            });
    }

    // 목격 게시글 수정

//    public WitnessResponse updateWitness(Long id, WitnessRequest req) {
//        log.info("목격 게시글 수정 요청 (ID: {}, 데이터: {})", id, req);
//
//        Integer currentUserId = (Integer) SecurityContextHolder.getContext()
//            .getAuthentication().getPrincipal();
//
//        Missing post = missingRepository.findById(id)
//            .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
//
//        if (!post.getMember().getId().equals(currentUserId)) {
//            log.warn("수정 권한 없음 - 요청자 ID: {}, 작성자 ID: {}", currentUserId, post.getMember().getId());
//            throw new RuntimeException("게시글 작성자만 수정할 수 있습니다.");
//        }
//
//        // postType 고정
//        post.setPostType(Missing.PostType.witness);
//
//        // 기존 사진 유지
//        if (req.photoUrls == null || req.photoUrls.isBlank()) {
//            req.photoUrls = post.getPhotoUrl();
//        }
//
//        post.setPhotoUrl(req.photoUrls);
//        post.setMissingDatetime(req.witnessDatetime);
//        post.setMissingLocation(req.witnessLocation);
//        post.setDetailDescription(req.detailDescription);
//
//        Missing updated = missingRepository.save(post);
//        log.info("목격 게시글 수정 완료 (ID: {})", updated.getId());
//
//        return WitnessResponse.from(updated);
//    }


    // 목격 불러오기 비즈니스 로직 (예찬)
    public List<WitnessResponse> getAllWitnessPosts() {
        return missingRepository.findByPostType(Missing.PostType.witness).stream()
            .map(WitnessResponse::from)
            .collect(Collectors.toList());
    }
}

