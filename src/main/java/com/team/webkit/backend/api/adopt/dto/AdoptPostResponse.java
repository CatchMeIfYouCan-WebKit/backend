package com.team.webkit.backend.api.adopt.dto;

import com.team.webkit.backend.api.adopt.entity.AdoptPost;
import com.team.webkit.backend.api.adopt.entity.AdoptPost.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
public class AdoptPostResponse {

    private Long id;
    private String title;
    private boolean vetVerified;
    private String comments;
    private String adoptLocation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 추가된 필드
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Status status;  // String → enum 타입으로 변경

    // 등록된 pet일 경우
    private PetSimpleDto pet;

    // 이미지 정보
    private String photoPath;             // 대표 이미지
    private List<String> photoPaths;      // 전체 이미지들

    // 직접입력 정보
    private String name;
    private String breed;
    private String coatColor;
    private String gender;
    private Boolean isNeutered;
    private LocalDate dateOfBirth;
    private Integer age;
    private BigDecimal weight;
    private String registrationNumber;

    private MemberSimpleDto member;

    public static AdoptPostResponse from(AdoptPost post) {
        AdoptPostResponse res = new AdoptPostResponse();
        res.setId(post.getId());
        res.setTitle(post.getTitle());
        res.setVetVerified(post.isVetVerified());
        res.setComments(post.getComments());
        res.setAdoptLocation(post.getAdoptLocation());
        res.setCreatedAt(post.getCreatedAt());
        res.setUpdatedAt(post.getUpdatedAt());
        res.setLatitude(post.getLatitude());
        res.setLongitude(post.getLongitude());
        res.setStatus(post.getStatus()); // enum이 아닌 문자열로 정의돼 있다고 가정

        // 작성자 정보
        res.setMember(MemberSimpleDto.from(post.getMember()));

        // pet 정보 (optional)
        if (post.getPet() != null) {
            res.setPet(PetSimpleDto.from(post.getPet()));
        }

        // 이미지 경로
        if (post.getPhotoPath() != null && !post.getPhotoPath().isBlank()) {
            List<String> paths = Arrays.asList(post.getPhotoPath().split(","));
            res.setPhotoPaths(paths);
            res.setPhotoPath(paths.get(0)); // 첫 번째 이미지를 대표 이미지로
        }

        // 직접입력 정보
        res.setName(post.getName());
        res.setBreed(post.getBreed());
        res.setCoatColor(post.getCoatColor());
        res.setGender(post.getGender());
        res.setIsNeutered(post.getIsNeutered());
        res.setDateOfBirth(post.getDateOfBirth());
        res.setAge(post.getAge());
        res.setWeight(post.getWeight());
        res.setRegistrationNumber(post.getRegistrationNumber());

        return res;
    }
}
