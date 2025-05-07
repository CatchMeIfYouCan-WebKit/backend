package com.team.webkit.backend.api.missing.dto;

import com.team.webkit.backend.api.missing.entity.Missing;
import java.time.LocalDateTime;

public class MissingResponse {

    public Long id;
    public String postType;
    public String photoUrl;
    public LocalDateTime missingDatetime;
    public String missingLocation;
    public String detailDescription;
    public String comments;
    public Integer userId;
    public Integer petId;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public static MissingResponse from(Missing post) {
        MissingResponse res = new MissingResponse();

        res.id = post.getId();
        res.postType = post.getPostType().name();
        res.photoUrl = post.getPhotoUrl();
        res.missingDatetime = post.getMissingDatetime();
        res.missingLocation = post.getMissingLocation();
        res.detailDescription = post.getDetailDescription();
        res.comments = post.getComments();
        res.userId = post.getMember().getId();
        res.petId = post.getPet().getId();
        res.createdAt = post.getCreatedAt();
        res.updatedAt = post.getUpdatedAt();

        return res;
    }
}
