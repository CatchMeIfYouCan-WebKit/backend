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
    public AdoptPostResponse create(AdoptPostRequest request, List<MultipartFile> files) {
        Member member = memberRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        AdoptPost post = new AdoptPost();
        post.setMember(member);
        post.setTitle(request.getTitle());
        post.setVetVerified(request.isVetVerified());
        post.setComments(request.getComments());
        post.setAdoptLocation(request.getAdoptLocation());

        // ✅ 1. petId가 있을 경우, 관계만 연결 (데이터는 request 기준으로)
        if (request.getPetId() != null) {
            Pet pet = petRepository.findById(request.getPetId())
                    .orElseThrow(() -> new IllegalArgumentException("반려동물 없음"));
            post.setPet(pet); // 관계만 설정
        }

        // ✅ 2. request 기준으로 모든 값 저장
        post.setName(request.getName());
        post.setBreed(request.getBreed());
        post.setCoatColor(request.getCoatColor());
        post.setGender(request.getGender());
        post.setIsNeutered(request.getIsNeutered());
        post.setDateOfBirth(request.getDateOfBirth());

        // 🎯 나이 계산
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

        // ✅ 3. 이미지 처리
        List<String> allImagePaths = new ArrayList<>();

        // photoPath가 문자열로 넘어온 경우
        if (request.getPhotoPath() != null && !request.getPhotoPath().isBlank()) {
            allImagePaths.addAll(Arrays.asList(request.getPhotoPath().split(",")));
        }

        // 업로드된 이미지 파일 저장
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String savedPath = fileService.saveAdoptImage(file);
                    allImagePaths.add(savedPath);
                }
            }
        }

        // 저장
        if (!allImagePaths.isEmpty()) {
            post.setPhotoPath(String.join(",", allImagePaths));
        }

        return AdoptPostResponse.from(adoptPostRepository.save(post));
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
    public AdoptPostResponse update(Long id, AdoptPostRequest request, List<MultipartFile> files, List<String> keepImages) {
        AdoptPost post = adoptPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        List<String> imagePaths = new ArrayList<>();

        // ✅ 기존 이미지 유지 목록 추가
        if (keepImages != null && !keepImages.isEmpty()) {
            imagePaths.addAll(keepImages);
        }

        // ✅ 새로 업로드된 파일 처리
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String savedPath = fileService.saveAdoptImage(file);
                    imagePaths.add(savedPath);
                }
            }
        }

        // ✅ 이미지 경로 최종 반영
        post.setPhotoPath(String.join(",", imagePaths));

        // ✅ 나머지 필드 업데이트
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
}

