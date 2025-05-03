package com.team.webkit.backend.api.map.DTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShelterResponse {
    private String shelterName;
    private String phone;
    private String address;
    private List<ShelterAnimalSummary> animalSummaries;
    private int animalCount;
}

