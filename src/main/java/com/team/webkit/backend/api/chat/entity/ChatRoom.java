package com.team.webkit.backend.api.chat.entity;

import com.team.webkit.backend.api.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "chat_rooms", // ✅ 실제 DB 테이블 이름과 일치시킴
        uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id", "type", "related_id"})
)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 참여자 A
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id")
    private Member user1;

    // 참여자 B
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id")
    private Member user2;

    // ADOPTION 또는 VET
    @Enumerated(EnumType.STRING)
    private ChatType type;

    // 입양 게시글 or 진료 예약과 연동됨
    @Column(name = "related_id")
    private Long relatedId;

    private LocalDateTime createdAt;

    public enum ChatType {
        ADOPTION,
        VET
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
