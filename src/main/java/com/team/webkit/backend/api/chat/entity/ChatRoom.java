package com.team.webkit.backend.api.chat.entity;

import com.team.webkit.backend.api.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_rooms",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id","user2_id","type","related_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private Member user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private Member user2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(name = "related_id", nullable = false)
    private Long relatedId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // --- 여기를 추가: 메시지 목록 매핑 ---
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sentAt ASC")
    private List<ChatMessage> messages = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public enum Type {
        ADOPTION, VET
    }
}
