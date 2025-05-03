package com.team.webkit.backend.api.map.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShelterResponse {
    private String shelterName;
    private String phone;
    private String address;
}

