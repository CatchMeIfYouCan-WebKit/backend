package com.team.webkit.backend.api.map.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "animal_hospitals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimalHospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 병원명
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    // 전화번호
    @Column(name = "phone", length = 20)
    private String phone;

    // 주소 (마커 위치 기반)
    @Column(name = "address", length = 255)
    private String address;

    // 인허가번호
    @Column(name = "license_number", nullable = false, unique = true, length = 50)
    private String licenseNumber;

    // 생성일시
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // 수정일시
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
