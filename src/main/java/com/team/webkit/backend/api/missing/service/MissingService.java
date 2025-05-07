package com.team.webkit.backend.api.missing.service;

import com.team.webkit.backend.api.member.repository.MemberRepository;
import com.team.webkit.backend.api.missing.dto.MissingRequest;
import com.team.webkit.backend.api.missing.dto.MissingResponse;
import com.team.webkit.backend.api.missing.dto.WitnessRequest;
import com.team.webkit.backend.api.missing.dto.WitnessResponse;
import com.team.webkit.backend.api.missing.entity.Missing;
import com.team.webkit.backend.api.missing.repository.MissingRepository;
import com.team.webkit.backend.api.pet.repository.PetRepository;
import java.util.List;
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
    public MissingResponse create(MissingRequest req) {
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

    // 목격 게시글 등록
    public WitnessResponse createWitness(WitnessRequest req, Integer userId) {
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
        post.setPhotoUrl(req.photoUrl);
        post.setMissingDatetime(req.witnessDatetime);
        post.setMissingLocation(req.witnessLocation);
        post.setDetailDescription(req.detailDescription);

        Missing saved = missingRepository.save(post);

        return WitnessResponse.from(saved);
    }


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

    // 실종 게시글 수정
    public MissingResponse updateMissing(Long id, MissingRequest req) {
        log.info("게시글 수정 요청 (ID: {}, 데이터: {})", id, req);

        Integer currentUserId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        Missing post = missingRepository.findById(id)
            .orElseThrow(() -> {
                log.error("게시글 없음 - ID: {}", id);
                return new RuntimeException("해당 게시글을 찾을 수 없습니다.");
            });

        if (!post.getMember().getId().equals(currentUserId)) {
            log.warn("수정 권한 없음 - 요청자 ID: {}, 게시글 작성자 ID: {}", currentUserId,
                post.getMember().getId());
            throw new RuntimeException("게시글 작성자만 수정할 수 있습니다.");
        }

        if (req.photoUrl == null || req.photoUrl.isBlank()) {
            req.photoUrl = post.getPhotoUrl();
        }

        try {
            post.setPostType(Missing.PostType.valueOf(req.postType));
        } catch (IllegalArgumentException e) {
            log.error("유효하지 않은 postType 값: {}", req.postType);
            throw new RuntimeException("postType 값이 잘못되었습니다.");
        }

        post.setPhotoUrl(req.photoUrl);
        post.setMissingDatetime(req.missingDatetime);
        post.setMissingLocation(req.missingLocation);
        post.setDetailDescription(req.detailDescription);

        Missing updated = missingRepository.save(post);
        log.info("게시글 수정 완료 (ID: {})", updated.getId());

        return MissingResponse.from(updated);
    }

    // 목격 게시글 수정
    public WitnessResponse updateWitness(Long id, WitnessRequest req) {
        log.info("목격 게시글 수정 요청 (ID: {}, 데이터: {})", id, req);

        Integer currentUserId = (Integer) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        Missing post = missingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));

        if (!post.getMember().getId().equals(currentUserId)) {
            log.warn("수정 권한 없음 - 요청자 ID: {}, 작성자 ID: {}", currentUserId, post.getMember().getId());
            throw new RuntimeException("게시글 작성자만 수정할 수 있습니다.");
        }

        // postType 고정
        post.setPostType(Missing.PostType.witness);

        // 기존 사진 유지
        if (req.photoUrl == null || req.photoUrl.isBlank()) {
            req.photoUrl = post.getPhotoUrl();
        }

        post.setPhotoUrl(req.photoUrl);
        post.setMissingDatetime(req.witnessDatetime);
        post.setMissingLocation(req.witnessLocation);
        post.setDetailDescription(req.detailDescription);

        Missing updated = missingRepository.save(post);
        log.info("목격 게시글 수정 완료 (ID: {})", updated.getId());

        return WitnessResponse.from(updated);
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
