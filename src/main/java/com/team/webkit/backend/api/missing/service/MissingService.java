package com.team.webkit.backend.api.missing.service;

import com.team.webkit.backend.api.member.repository.MemberRepository;
import com.team.webkit.backend.api.missing.dto.MissingRequest;
import com.team.webkit.backend.api.missing.dto.MissingResponse;
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
public class MissingService {

    private final MissingRepository missingRepository;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    // 실종 게시글 등록
    public MissingResponse createMissing(MissingRequest req) {
        log.info("실종 게시글 등록 요청: {}", req);

        // 🔐 현재 로그인한 사용자 ID 가져오기
        Integer currentUserId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        var member = memberRepository.findById(currentUserId)
            .orElseThrow(() -> {
                log.error("사용자 없음 - ID: {}", currentUserId);
                return new RuntimeException("사용자를 찾을 수 없습니다.");
            });

        var pet = petRepository.findById(req.petId)
            .orElseThrow(() -> {
                log.error("반려동물 없음 - ID: {}", req.petId);
                return new RuntimeException("반려동물을 찾을 수 없습니다.");
            });

        Missing post = new Missing();
        post.setMember(member);
        post.setPet(pet);
        post.setPostType(Missing.PostType.valueOf(req.postType));
        post.setPhotoUrl(req.photoUrl);
        post.setMissingDatetime(req.missingDatetime);
        post.setMissingLocation(req.missingLocation);
        post.setDetailDescription(req.detailDescription);

        Missing saved = missingRepository.save(post);

        log.info("실종 게시글 등록 완료 (ID: {})", saved.getId());

        return MissingResponse.from(saved);
    }


    // 실종 게시글 상세조회
    public MissingResponse getMissingPostById(Long id) {
        log.info("실종 게시글 상세 조회 요청: 게시글 ID={}", id);

        return missingRepository.findById(id)
            .map(post -> {
                MissingResponse response = MissingResponse.from(post);
                log.info(
                    "게시글 조회 성공: [ID: {}], [작성자 ID: {}], [PostType: {}], [UserID: {}], [PetID: {}], [PhotoURL: {}], "
                        +
                        "[MissingDatetime: {}], [MissingLocation: {}], [DetailDescription: {}], [Address: {}], "
                        +
                        "[Breed: {}], [CoatColor: {}], [CreatedAt: {}], [UpdatedAt: {}], [UserNickname: {}], [UserPhone: {}]",
                    response.id,
                    response.userId,
                    response.postType,
                    response.userId,
                    response.petId,
                    response.photoUrl,
                    response.missingDatetime,
                    response.missingLocation,
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

//    // 실종 게시글 수정
//    public MissingResponse updateMissing(Long id, MissingRequest req) {
//        log.info("게시글 수정 요청 (ID: {}, 데이터: {})", id, req);
//
//        Integer currentUserId = (Integer) SecurityContextHolder.getContext().getAuthentication()
//            .getPrincipal();
//
//        Missing post = missingRepository.findById(id)
//            .orElseThrow(() -> {
//                log.error("게시글 없음 - ID: {}", id);
//                return new RuntimeException("해당 게시글을 찾을 수 없습니다.");
//            });
//
//        if (!post.getMember().getId().equals(currentUserId)) {
//            log.warn("수정 권한 없음 - 요청자 ID: {}, 게시글 작성자 ID: {}", currentUserId,
//                post.getMember().getId());
//            throw new RuntimeException("게시글 작성자만 수정할 수 있습니다.");
//        }
//
//        if (req.photoUrl == null || req.photoUrl.isBlank()) {
//            req.photoUrl = post.getPhotoUrl();
//        }
//
//        try {
//            post.setPostType(Missing.PostType.valueOf(req.postType));
//        } catch (IllegalArgumentException e) {
//            log.error("유효하지 않은 postType 값: {}", req.postType);
//            throw new RuntimeException("postType 값이 잘못되었습니다.");
//        }
//
//        post.setPhotoUrl(req.photoUrl);
//        post.setMissingDatetime(req.missingDatetime);
//        post.setMissingLocation(req.missingLocation);
//        post.setDetailDescription(req.detailDescription);
//
//        Missing updated = missingRepository.save(post);
//        log.info("게시글 수정 완료 (ID: {})", updated.getId());
//
//        return MissingResponse.from(updated);
//    }


    //실종 불러오기 데이터 비즈니스 로직 추가(예찬)
    public List<MissingResponse> getAllMissingPosts() {
        return missingRepository.findByPostType(Missing.PostType.missing)
            .stream()
            .map(MissingResponse::from)
            .collect(Collectors.toList());
    }


}
