package com.team.webkit.backend.api.map.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShelterMissingResponseDto {

    private Integer id;
    private String announcementNo;
    private String animalType;
    private String breed;
    private String coatColor;
    private String gender;
    private String neutered;
    private String characteristics;
    private LocalDate rescueDate;
    private String rescueReason;
    private String rescueLocation;
    private LocalDate announceStart;
    private LocalDate announceEnd;
    private String shelterName;
    private String representative;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String remarks;
    private String imageUrl;

}
