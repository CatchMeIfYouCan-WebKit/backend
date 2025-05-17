package com.team.webkit.backend.api.missing.entity;

import com.team.webkit.backend.api.member.entity.Member;
import com.team.webkit.backend.api.pet.entity.Pet;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) // 또는 EAGER
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public enum PostType {
        missing, witness
    }

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "missing_datetime")
    @NotNull(message = "시간 입력은 필수입니다.")
    private LocalDateTime missingDatetime;

    @Column(name = "missing_location")
    @NotBlank(message = "위치 입력은 필수입니다.")
    private String missingLocation;

    @Column(name = "detail_description")
    @NotBlank(message = "상세설명 입력은 필수입니다.")
    private String detailDescription;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

//위도 경도 추가(예찬)
    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Transient
    private String predictedBreed;

    @Transient
    private String predictedColor;



    public List<String> getPhotoUrls() {
        if (photoUrl == null || photoUrl.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(photoUrl.split(","));
    }
}
