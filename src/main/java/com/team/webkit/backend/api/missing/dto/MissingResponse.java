package com.team.webkit.backend.api.missing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team.webkit.backend.api.missing.entity.Missing;
import java.time.LocalDateTime;

public class MissingResponse {

    public Long id;

    public String postType;

    public Integer commentCount;

    public Integer userId;

    public String userNickname;

    public String userPhone;

    public Integer petId;

    public String petName;

    public String petBreed;

    public String petCoatColor;

    public String photoUrl;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime missingDatetime;

    public String missingLocation;

    public String detailDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime updatedAt;

    public String predictedBreed;
    public String predictedColor;
    public Double distance; // km 단위


    public static MissingResponse from(Missing post, Double distanceKm) {
        MissingResponse res = from(post); // 기존 필드 채우기

        res.distance = distanceKm; // 거리 정보 추가
        return res;
    }

    public static MissingResponse from(Missing post) {
        MissingResponse res = new MissingResponse();

        res.id = post.getId();
        res.postType = post.getPostType().name();
        res.userId = post.getMember().getId();
        res.userNickname = post.getMember().getNickname();
        res.userPhone = post.getMember().getPhone();
        res.photoUrl = post.getPhotoUrl();
        res.missingDatetime = post.getMissingDatetime();
        res.missingLocation = post.getMissingLocation();
        res.detailDescription = post.getDetailDescription();
        res.createdAt = post.getCreatedAt();
        res.updatedAt = post.getUpdatedAt();

        // 실종 게시글만 pet 정보 포함
        if (post.getPostType() == Missing.PostType.missing && post.getPet() != null) {
            res.petId = post.getPet().getId();
            res.petName = post.getPet().getName();
            res.petBreed = post.getPet().getBreed();
            res.petCoatColor = post.getPet().getCoatColor();
        }

        // 👉 추천일 경우, 예측 품종/색상 포함 (추가된 필드)
        res.predictedBreed = post.getPredictedBreed();     // 아래에 추가 설명
        res.predictedColor = post.getPredictedColor();


        return res;
    }
}
