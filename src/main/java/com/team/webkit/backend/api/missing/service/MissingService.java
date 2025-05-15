package com.team.webkit.backend.api.missing.service;

import com.team.webkit.backend.api.comment.repository.CommentRepository;
import com.team.webkit.backend.api.member.repository.MemberRepository;
import com.team.webkit.backend.api.missing.dto.MissingRequest;
import com.team.webkit.backend.api.missing.dto.MissingResponse;
import com.team.webkit.backend.api.missing.entity.Missing;
import com.team.webkit.backend.api.missing.entity.Missing.PostType;
import com.team.webkit.backend.api.missing.repository.MissingRepository;
import com.team.webkit.backend.api.pet.repository.PetRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissingService {

    private final MissingRepository missingRepository;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final CommentRepository commentRepository;

    // 실종 등록
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


    // 실종 전체조회
    public List<MissingResponse> getAllMissingPosts() {
        List<Missing> posts = missingRepository.findByPostType(PostType.missing);

        return posts.stream()
            .map(post -> {
                MissingResponse res = MissingResponse.from(post);
                res.commentCount = commentRepository.countByPost_Id(post.getId());
                return res;
            })
            .collect(Collectors.toList());
    }


    // 실종 상세조회
    public MissingResponse getMissingPostById(Long id) {
        log.info("실종 게시글 상세 조회 요청: 게시글 ID={}", id);

        return missingRepository.findById(id)
            .map(post -> {
                MissingResponse response = MissingResponse.from(post);

                Integer petId = response.petId != null ? response.petId : -1;
                String petBreed = response.petBreed != null ? response.petBreed : "정보 없음";
                String petCoatColor =
                    response.petCoatColor != null ? response.petCoatColor : "정보 없음";

                log.info(
                    "게시글 조회 성공: [ID: {}], [작성자 ID: {}], [PostType: {}], [PetID: {}], [PhotoURL: {}], "
                        +
                        "[MissingDatetime: {}], [MissingLocation: {}], [DetailDescription: {}] " +
                        "[Breed: {}], [CoatColor: {}], [CreatedAt: {}], [UpdatedAt: {}], [UserNickname: {}], [UserPhone: {}]",

                    response.id,
                    response.userId,
                    response.postType,
                    petId,
                    response.photoUrl,
                    response.missingDatetime,
                    response.missingLocation,
                    response.detailDescription,
                    petBreed,
                    petCoatColor,
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


    // 실종 삭제
    public void deleteMissingPost(Long id) {
        Missing post = missingRepository.findById(id)
            .orElseThrow(() -> {
                log.error("삭제 실패: 존재하지 않는 게시글 ID={}", id);
                return new RuntimeException("게시글을 찾을 수 없습니다.");
            });

        if (post.getPet() == null) {
            log.warn("삭제 실패: petId가 없어 삭제할 수 없음. 게시글 ID={}", id);
            throw new RuntimeException("petId가 없어 삭제할 수 없습니다.");
        }

        missingRepository.delete(post);
        log.info("게시글 삭제 성공: 게시글 ID={}", id);
    }

    // 실종 필터링(품종, 털색)
    public List<MissingResponse> findMissingPostsByFilter(String breed, String coatColor) {
        List<Missing> posts;

        if (breed != null && coatColor != null) {
            posts = missingRepository.findByPetBreedAndPetCoatColor(breed, coatColor);
        } else if (breed != null) {
            posts = missingRepository.findByPetBreed(breed);
        } else if (coatColor != null) {
            posts = missingRepository.findByPetCoatColor(coatColor);
        } else {
            posts = missingRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        log.info("필터링된 게시글 개수: {}", posts.size());
        return posts.stream()
            .map(MissingResponse::from)
            .collect(Collectors.toList());
    }


}
