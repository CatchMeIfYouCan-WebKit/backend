package com.team.webkit.backend.api.pet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.team.webkit.backend.api.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Member member;

    @JsonProperty("user_id")
    public Integer getUserId() {
        return member != null ? member.getId() : null;
    }

    //    @Column(name = "photo_path", nullable = false, length = 500)
    @NotBlank(message = "사진은 필수입니다.")
    private String photoPath;

    @Column(name = "name", nullable = false, length = 50)
    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @Column(name = "breed", nullable = false, length = 50)
    @NotBlank(message = "종은 필수입니다.")
    private String breed;

    @Column(name = "coat_color", length = 50)
    @NotBlank(message = "색깔은 필수입니다.")
    private String coatColor;

    @Column(name = "gender")
    @NotNull(message = "성별은 필수입니다.")
    private String gender;

    @Column(name = "is_neutered", nullable = false)
    @NotNull(message = "중성화 여부는 필수입니다.")
    private Boolean isNeutered = false;

    @Column(name = "date_of_birth", nullable = false)
    @NotNull(message = "생일은 필수입니다.")
    private LocalDate dateOfBirth;

    @Column(name = "age")
    @NotNull(message = "나이는 필수입니다.")
    private Integer age;

    @Column(name = "weight", precision = 5, scale = 2)
    @NotNull(message = "몸무게는 필수입니다.")
    private BigDecimal weight;

    @Column(name = "registration_number", unique = true, length = 100)
    private String registrationNumber;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
