package com.team.webkit.backend.api.vet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team.webkit.backend.api.vet.entity.Vet;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VetResponseDto {

    private Integer id;

    private String loginId;

    private String phone;

    private String name;

    private String certificate;

    private String licenseNumber;

    private Integer hospitalId;

    private String career;

    private String introduction;

    private String specialties;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static VetResponseDto fromEntity(Vet vet) {
        VetResponseDto dto = new VetResponseDto();

        dto.id = vet.getId();
        dto.loginId = vet.getLoginId();
        dto.phone = vet.getPhone();
        dto.name = vet.getName();
        dto.certificate = vet.getCertificate();
        dto.licenseNumber = vet.getLicenseNumber();
        dto.hospitalId = vet.getHospitalId();
        dto.career = vet.getCareer();
        dto.introduction = vet.getIntroduction();
        dto.specialties = vet.getSpecialties();
        dto.createdAt = vet.getCreatedAt();
        dto.updatedAt = vet.getUpdatedAt();

        return dto;
    }
}
