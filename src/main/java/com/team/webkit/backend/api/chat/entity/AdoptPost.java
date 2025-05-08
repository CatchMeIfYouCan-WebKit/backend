package com.team.webkit.backend.api.chat.entity;

import com.team.webkit.backend.api.member.entity.Member;
import com.team.webkit.backend.api.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "adopt_posts")
public class AdoptPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "is_vet_verified", nullable = false)
    private boolean isVetVerified;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(name = "adopt_location", nullable = false, length = 255)
    private String adoptLocation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
