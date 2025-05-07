package com.team.webkit.backend.api.missing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team.webkit.backend.api.missing.entity.Missing;
import java.time.LocalDateTime;

public class MissingResponse {

    public Long id;

    public String postType;

    public Integer userId;

    public Integer petId;

    public String photoUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime missingDatetime;

    public String missingLocation;

    public String detailDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime updatedAt;


    public static MissingResponse from(Missing post) {
        MissingResponse res = new MissingResponse();

        res.id = post.getId();
        res.postType = post.getPostType().name();
        res.userId = post.getMember().getId();
        res.petId = post.getPet().getId();
        res.photoUrl = post.getPhotoUrl();
        res.missingDatetime = post.getMissingDatetime();
        res.missingLocation = post.getMissingLocation();
        res.detailDescription = post.getDetailDescription();
        res.createdAt = post.getCreatedAt();
        res.updatedAt = post.getUpdatedAt();

        return res;
    }
}
