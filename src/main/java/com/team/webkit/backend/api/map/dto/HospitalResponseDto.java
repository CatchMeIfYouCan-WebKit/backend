package com.team.webkit.backend.api.map.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HospitalResponseDto {

    private Integer id;
    private String name;
    private String phone;
    private String address;
    private String licenseNumber;
    private Double latitude;
    private Double longitude;

}
