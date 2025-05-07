package com.team.webkit.backend.api.comment.dto;

import com.team.webkit.backend.api.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {

    private Long id;
    private String content;
    private Long parentCommentId;
    private Integer userId;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentResponse from(Comment comment) {
        CommentResponse dto = new CommentResponse();

        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setParentCommentId(comment.getParent() != null ? comment.getParent().getId() : null);
        dto.setUserId(comment.getMember().getId());
        dto.setPostId(comment.getPost().getId());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());

        return dto;
    }
}