package com.team.webkit.backend.api.adopt.service;

import com.team.webkit.backend.api.adopt.dto.AdoptPostRequest;
import com.team.webkit.backend.api.adopt.dto.AdoptPostResponse;
import com.team.webkit.backend.api.adopt.entity.AdoptPost;
import com.team.webkit.backend.api.adopt.repository.AdoptPostRepository;
import com.team.webkit.backend.api.member.entity.Member;
import com.team.webkit.backend.api.member.repository.MemberRepository;
import com.team.webkit.backend.api.pet.entity.Pet;
import com.team.webkit.backend.api.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.team.webkit.backend.support.FileService;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdoptPostService {

    private final AdoptPostRepository adoptPostRepository;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final FileService fileService;


    @Transactional
    public AdoptPostResponse create(AdoptPostRequest request) {
        Member member = memberRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        AdoptPost post = new AdoptPost();
        post.setMember(member);
        post.setTitle(request.getTitle());
        post.setVetVerified(request.isVetVerified());
        post.setComments(request.getComments());
        post.setAdoptLocation(request.getAdoptLocation());

        // ✅ 등록된 petId가 있다면 관계만 연결
        if (request.getPetId() != null) {
            Pet pet = petRepository.findById(request.getPetId())
                    .orElseThrow(() -> new IllegalArgumentException("반려동물 없음"));
            post.setPet(pet);
        }

        // ✅ 직접입력 정보 설정
        post.setName(request.getName());
        post.setBreed(request.getBreed());
        post.setCoatColor(request.getCoatColor());
        post.setGender(request.getGender());
        post.setIsNeutered(request.getIsNeutered());
        post.setDateOfBirth(request.getDateOfBirth());

        if (request.getDateOfBirth() != null) {
            int currentYear = java.time.LocalDate.now().getYear();
            int birthYear = request.getDateOfBirth().getYear();
            post.setAge(currentYear - birthYear);
        }

        post.setWeight(request.getWeight());
        post.setRegistrationNumber(request.getRegistrationNumber());
        post.setLatitude(request.getLatitude());
        post.setLongitude(request.getLongitude());
        post.setStatus(AdoptPost.Status.valueOf(request.getStatus()));

        // ✅ 이미지 경로만 설정 (이미 upload API로 업로드 완료된 경로라고 가정)
        if (request.getPhotoPath() != null && !request.getPhotoPath().isBlank()) {
            post.setPhotoPath(request.getPhotoPath());
        }

        AdoptPost saved = adoptPostRepository.save(post);
        return AdoptPostResponse.from(saved);
    }






    // ✅ 2. 전체 조회
    @Transactional(readOnly = true)
    public List<AdoptPostResponse> getAll() {
        return adoptPostRepository.findAll()
                .stream()
                .map(AdoptPostResponse::from)
                .collect(Collectors.toList());
    }
    //필터링 조회
    @Transactional(readOnly = true)
    public List<AdoptPostResponse> getFiltered(String region, Integer age, String breed, String color) {
        return adoptPostRepository.findByFilter(region, age, breed, color)
                .stream()
                .map(AdoptPostResponse::from)
                .collect(Collectors.toList());
    }



    // ✅ 3. 수정 (PUT)
    @Transactional
    public AdoptPostResponse update(Long id, AdoptPostRequest request, List<String> keepImages) {
        AdoptPost post = adoptPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        List<String> imagePaths = new ArrayList<>();

        // ✅ 유지할 이미지 경로 반영
        if (keepImages != null && !keepImages.isEmpty()) {
            imagePaths.addAll(keepImages);
        }

        // ✅ adoptPostRequest.photoPath에도 새 이미지 경로가 있다면 추가
        if (request.getPhotoPath() != null && !request.getPhotoPath().isBlank()) {
            imagePaths.addAll(Arrays.asList(request.getPhotoPath().split(",")));
        }

        // ✅ 최종 이미지 경로 저장
        post.setPhotoPath(String.join(",", imagePaths));

        // ✅ 나머지 필드들 업데이트
        if (request.getPetId() != null) {
            Pet pet = petRepository.findById(request.getPetId())
                    .orElseThrow(() -> new IllegalArgumentException("펫 없음"));
            post.setPet(pet);
        }

        if (request.getName() != null) post.setName(request.getName());
        if (request.getBreed() != null) post.setBreed(request.getBreed());
        if (request.getCoatColor() != null) post.setCoatColor(request.getCoatColor());
        if (request.getGender() != null) post.setGender(request.getGender());
        if (request.getIsNeutered() != null) post.setIsNeutered(request.getIsNeutered());

        if (request.getDateOfBirth() != null) {
            post.setDateOfBirth(request.getDateOfBirth());
            int currentYear = java.time.LocalDate.now().getYear();
            int birthYear = request.getDateOfBirth().getYear();
            post.setAge(currentYear - birthYear);
        }

        if (request.getWeight() != null) post.setWeight(request.getWeight());
        if (request.getRegistrationNumber() != null) post.setRegistrationNumber(request.getRegistrationNumber());
        if (request.getLatitude() != null) post.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) post.setLongitude(request.getLongitude());
        if (request.getStatus() != null) post.setStatus(AdoptPost.Status.valueOf(request.getStatus()));

        if (request.getTitle() != null) post.setTitle(request.getTitle());
        if (request.getComments() != null) post.setComments(request.getComments());
        post.setVetVerified(request.isVetVerified());

        return AdoptPostResponse.from(post);
    }







    // ✅ 4. 삭제 (DELETE)
    @Transactional
    public void delete(Long id) {
        adoptPostRepository.deleteById(id);
    }

    @Transactional
    public AdoptPostResponse updateStatus(Long id, String newStatus) {
        AdoptPost post = adoptPostRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + id));
        // enum 값 매핑
        post.setStatus(AdoptPost.Status.valueOf(newStatus));
        // 변경 감지(dirty-checking)로 자동 저장
        return AdoptPostResponse.from(post);
    }
}

