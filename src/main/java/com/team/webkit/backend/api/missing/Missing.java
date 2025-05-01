package com.team.webkit.backend.api.missing;

/* Todo
1. 테이블명 바꾸기
2. 클래스명 바꾸기 (Board -> ?)
3. 테이블 컬럼 변경
    - 다른 테이블과 join(user, pet)
    - 삭제되면? 조회수? 좋아요/추천?
*/

import com.team.webkit.backend.api.member.Member;
import com.team.webkit.backend.api.pet.Pet;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "missing_posts")
public class Missing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType postType = PostType.missing;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "missing_datetime", nullable = false)
    private LocalDateTime missingDatetime;

    @Column(name = "missing_location", nullable = false)
    private String missingLocation;

    @Column(name = "detail_description", columnDefinition = "TEXT")
    private String detailDescription;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // FK: user_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    // FK: pet_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;
}