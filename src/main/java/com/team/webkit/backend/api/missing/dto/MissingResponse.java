package com.team.webkit.backend.api.missing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team.webkit.backend.api.missing.entity.Missing;
import java.time.LocalDateTime;

public class MissingResponse {

    public Long id;

    public String postType;

    public Integer userId;

    public String userNickname;

    public String userPhone;

    public Integer petId;

    public String photoUrl;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime missingDatetime;

    public String missingLocation;

    public String detailDescription;

    // 추가한 필드(예찬)
    public String address;
    public String breed;
    public String coatColor;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime updatedAt;


    public static MissingResponse from(Missing post) {
        MissingResponse res = new MissingResponse();

        res.id = post.getId();
        res.postType = post.getPostType().name();
        res.userId = post.getMember().getId();
        res.userNickname = post.getMember().getNickname();
        res.userPhone = post.getMember().getPhone();
        res.petId = post.getPet().getId();
        res.photoUrl = post.getPhotoUrl();
        res.missingDatetime = post.getMissingDatetime();
        res.missingLocation = post.getMissingLocation();
        res.detailDescription = post.getDetailDescription();
        res.createdAt = post.getCreatedAt();
        res.updatedAt = post.getUpdatedAt();
        //추가 필드 입니다(예찬)
        res.address = post.getMissingLocation(); // 주소로 사용
        res.breed = post.getPet().getBreed();
        res.coatColor = post.getPet().getCoatColor();

        return res;
    }
}
