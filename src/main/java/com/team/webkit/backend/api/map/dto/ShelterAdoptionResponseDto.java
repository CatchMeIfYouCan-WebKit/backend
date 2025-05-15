package com.team.webkit.backend.api.map.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShelterAdoptionResponseDto {

    private Integer id;
    private String announcementNo;
    private String animalRegNo;
    private String breed;
    private String color;
    private String gender;
    private String neutered;
    private String ageWeight;
    private String rescueCharacteristics;
    private String socialCharacteristics;
    private String healthCharacteristics;
    private String healthCheckup;
    private String vaccinationStatus;
    private String occurrencePlace;
    private LocalDateTime receptionDatetime;
    private String otherNotes;
    private String jurisdiction;
    private String status;
    private String shelterName;
    private String shelterContact;
    private String protectionLocation;
    private Double latitude;
    private Double longitude;
    private String adoptionProcedure;
    private String adoptionSupport;
    private String volunteerInfo;
    private String eventInfo;
    private String imageUrls;

}
