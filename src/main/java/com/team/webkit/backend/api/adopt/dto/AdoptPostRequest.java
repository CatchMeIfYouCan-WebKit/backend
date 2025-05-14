package com.team.webkit.backend.api.adopt.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AdoptPostRequest {

    private Integer userId;

    // 선택형 등록: 등록된 동물 ID (없으면 직접입력 필드 사용)
    private Integer petId;

    // 직접입력 동물 정보 (nullable)
    private String photoPath;
    private String name;
    private String breed;
    private String coatColor;
    private String gender;
    private Boolean isNeutered;
    private LocalDate dateOfBirth;
    private Integer age;
    private BigDecimal weight;
    private String registrationNumber;

    // 공통 필드
    private String title;
    private boolean vetVerified;
    private String comments;
    private String adoptLocation;

    // 추가 필드
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String status;  // ENUM: "분양중", "분양완료"

}
