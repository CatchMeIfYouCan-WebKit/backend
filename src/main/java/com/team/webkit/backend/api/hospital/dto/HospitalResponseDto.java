package com.team.webkit.backend.api.hospital.dto;

import com.team.webkit.backend.api.hospital.entity.Hospital;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalResponseDto {

    private Integer id;
    private String name;
    private String phone;
    private String address;
    private String licenseNumber;

    public static HospitalResponseDto fromEntity(Hospital hospital) {
        return new HospitalResponseDto(
            hospital.getId(),
            hospital.getName(),
            hospital.getPhone(),
            hospital.getAddress(),
            hospital.getLicenseNumber()
        );
    }
}
