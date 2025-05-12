package com.team.webkit.backend.api.vet.dto;

import com.team.webkit.backend.api.vet.entity.Vet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VetRequestDto {

    private String loginId; // 아이디
    private String password; // 비밀번호
    private String phone; // 전화번호
    private String name; // 이름
    private Boolean hasCertificate; // 자격증 소지 여부
    private String licenseNumber; // 자격 번호
    private String licenseImageUrl; // 자격증 사진
    private Integer hospitalId; // 병원 ID
    private String career; // 경력
    private String introduction; // 소개글
    private String specialties; // 진료 분야

    public Vet toEntity() {
        Vet vet = new Vet();

        vet.setLoginId(this.loginId);
        vet.setPassword(this.password);
        vet.setPhone(this.phone);
        vet.setName(this.name);
        vet.setHasCertificate(this.hasCertificate);
        vet.setLicenseNumber(this.licenseNumber);
        vet.setLicenseImageUrl(this.licenseImageUrl);
        vet.setHospitalId(this.hospitalId);
        vet.setCareer(this.career);
        vet.setIntroduction(this.introduction);
        vet.setSpecialties(this.specialties);

        return vet;
    }

}
