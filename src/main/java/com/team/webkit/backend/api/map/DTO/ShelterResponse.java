package com.team.webkit.backend.api.map.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

