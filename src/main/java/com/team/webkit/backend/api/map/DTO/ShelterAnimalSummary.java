package com.team.webkit.backend.api.map.DTO;

import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShelterAnimalSummary {
    private String breed;              // 품종
    private String coatColor;          // 색상
    private String gender;             // 성별
    private String neutered;           // 중성화 여부 ("예" / "아니오")
    private String status;             // 상태 ("공고중", "보호중" 등)
    private LocalDate announceEnd;     // 공고 종료일 (선택)
}
