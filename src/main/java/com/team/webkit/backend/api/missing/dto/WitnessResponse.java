package com.team.webkit.backend.api.missing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team.webkit.backend.api.missing.entity.Missing;
import java.time.LocalDateTime;

public class WitnessResponse {

    public Long id;

    public String postType;

    public Integer commentCount;

    public Integer userId;

    public String userNickname;

    public String userPhone;

    public String photoUrl;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime witnessDatetime;

    public String witnessLocation;

    public String detailDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime updatedAt;

    public static WitnessResponse from(Missing post) {
        WitnessResponse res = new WitnessResponse();

        res.id = post.getId();
        res.postType = post.getPostType().name();
        res.userId = post.getMember().getId();
        res.userNickname = post.getMember().getNickname();
        res.userPhone = post.getMember().getPhone();
        res.photoUrl = post.getPhotoUrl();
        res.witnessDatetime = post.getMissingDatetime();
        res.witnessLocation = post.getMissingLocation();
        res.detailDescription = post.getDetailDescription();
        res.createdAt = post.getCreatedAt();
        res.updatedAt = post.getUpdatedAt();

        return res;
    }
}
