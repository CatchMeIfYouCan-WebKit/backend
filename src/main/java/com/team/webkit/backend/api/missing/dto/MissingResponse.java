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


    public static MissingResponse from(Missing post) {
        if (post.getPet() == null) {
            throw new IllegalStateException("해당 게시글은 반려동물 정보가 없습니다.");
        }

        MissingResponse res = new MissingResponse();

        res.id = post.getId();
        res.postType = "missing";
        res.userId = post.getMember().getId();
        res.userNickname = post.getMember().getNickname();
        res.userPhone = post.getMember().getPhone();
        res.petId = post.getPet().getId();
        res.petName = post.getPet().getName();
        res.petBreed = post.getPet().getBreed();
        res.petCoatColor = post.getPet().getCoatColor();
        res.photoUrl = post.getPhotoUrl();
        res.missingDatetime = post.getMissingDatetime();
        res.missingLocation = post.getMissingLocation();
        res.detailDescription = post.getDetailDescription();
        res.createdAt = post.getCreatedAt();
        res.updatedAt = post.getUpdatedAt();

        return res;
    }
}
