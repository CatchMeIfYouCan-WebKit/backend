package com.team.webkit.backend.api.map.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HospitalResponse {

    private String name;
    private String phone;
    private String address;
}
