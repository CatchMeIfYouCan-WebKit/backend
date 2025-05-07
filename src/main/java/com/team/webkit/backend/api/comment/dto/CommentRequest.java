package com.team.webkit.backend.api.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    private Long postId;
    private Integer userId;
    private Long parentCommentId; // null이면 원댓글
    private String content;
}