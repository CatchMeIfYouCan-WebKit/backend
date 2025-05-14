package com.team.webkit.backend.api.chat.entity;

import com.team.webkit.backend.api.adopt.entity.AdoptPost;
import com.team.webkit.backend.api.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_rooms",
        uniqueConstraints = @UniqueConstraint(name = "unique_chat", columnNames = {"sender_id", "receiver_id", "adopt_post_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 보내는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    // 받는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    // 어떤 입양 게시글에서의 채팅인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopt_post_id", nullable = false)
    private AdoptPost adoptPost;

    @Column(name = "created_at", columnDefinition = "DATETIME", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
