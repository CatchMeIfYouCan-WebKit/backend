package com.team.webkit.backend.api.comment.repository;

import com.team.webkit.backend.api.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    int countByPost_Id(Long postId);
}