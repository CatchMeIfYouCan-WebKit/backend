package com.team.webkit.backend.api.missing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team.webkit.backend.api.missing.entity.Missing;
import java.time.LocalDateTime;

public class WitnessResponse {

    public Long id;

    public String postType;

    public Integer userId;

    public String photoUrl;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime witnessDatetime;

    public String witnessLocation;

    public String detailDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime updatedAt;

    //추가된 필드 입니다(예찬)
    public String address;     // witnessLocation을 복사해서 사용
    public String breed;
    public String coatColor;
    public static WitnessResponse from(Missing post) {
        WitnessResponse res = new WitnessResponse();

        res.id = post.getId();
        res.postType = post.getPostType().name();
        res.userId = post.getMember().getId();
        res.photoUrl = post.getPhotoUrl();
        res.witnessDatetime = post.getMissingDatetime();
        res.witnessLocation = post.getMissingLocation();
        res.detailDescription = post.getDetailDescription();
        res.createdAt = post.getCreatedAt();
        res.updatedAt = post.getUpdatedAt();

        res.address = post.getMissingLocation();             // 주소용 필드로 중복 사용
        res.breed = post.getPet().getBreed();
        res.coatColor = post.getPet().getCoatColor();

        return res;
    }
}
