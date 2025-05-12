package com.team.webkit.backend.api.vet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "vet")
public class Vet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login_id", nullable = false)
    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Column(name = "phone", nullable = false)
    @NotBlank(message = "휴대전화는 필수입니다.")
    private String phone;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "license_image_url")
    private String licenseImageUrl;

    @Column(name = "hospital_id")
    private Integer hospitalId;

    @Column(name = "name")
    private String name;

    @Column(name = "career")
    private String career;

    @Column(name = "has_certificate")
    private Boolean hasCertificate;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "specialties")
    private String specialties;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
