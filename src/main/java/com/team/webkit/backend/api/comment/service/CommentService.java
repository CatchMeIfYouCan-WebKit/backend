package com.team.webkit.backend.api.comment.service;

import com.team.webkit.backend.api.comment.dto.CommentRequest;
import com.team.webkit.backend.api.comment.dto.CommentResponse;
import com.team.webkit.backend.api.comment.entity.Comment;
import com.team.webkit.backend.api.comment.repository.CommentRepository;
import com.team.webkit.backend.api.member.repository.MemberRepository;
import com.team.webkit.backend.api.missing.repository.MissingRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final MissingRepository missingRepository;

    @Transactional
    public CommentResponse create(CommentRequest req) {
        log.info("댓글 등록 요청 - 게시글 ID: {}, 사용자 ID: {}, 내용: {}", req.getPostId(), req.getUserId(),
            req.getContent());

        Comment comment = new Comment();

        comment.setContent(req.getContent());
        comment.setPost(missingRepository.findById(req.getPostId()).orElseThrow());
        comment.setMember(memberRepository.findById(req.getUserId()).orElseThrow());

        if (req.getParentCommentId() != null) {
            Comment parent = commentRepository.findById(req.getParentCommentId()).orElseThrow();
            comment.setParent(parent);
            log.info("대댓글로 등록 - 부모 댓글 ID: {}", parent.getId());
        }

        Comment saved = commentRepository.save(comment);
        log.info("댓글 등록 완료 - ID: {}", saved.getId());

        return CommentResponse.from(saved);
    }

    public List<CommentResponse> getComments(Long postId) {
        log.info("게시글 ID {}의 댓글 목록 조회 요청", postId);

        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
            .stream().map(CommentResponse::from)
            .collect(Collectors.toList());
    }

    public void delete(Long id) {
        log.info("댓글 삭제 요청 - ID: {}", id);

        Comment comment = commentRepository.findById(id).orElseThrow();

        // 현재 로그인된 사용자
        Integer currentUserId = (Integer) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        // 댓글 작성자
        Integer commentWriterId = comment.getMember().getId();

        if (!commentWriterId.equals(currentUserId)) {
            log.warn("댓글 삭제 실패 - 작성자 불일치. 요청자 ID: {}, 댓글 작성자 ID: {}", currentUserId,
                commentWriterId);
            throw new RuntimeException("댓글 작성자만 삭제할 수 있습니다.");
        }

        commentRepository.deleteById(id);

        log.info("댓글 삭제 완료 - ID: {}", id);
    }
}
