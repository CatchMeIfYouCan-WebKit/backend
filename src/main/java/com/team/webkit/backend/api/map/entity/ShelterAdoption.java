package com.team.webkit.backend.api.map.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "shelter_adoption_animals")
public class ShelterAdoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "announcement_no", nullable = false, unique = true)
    private String announcementNo;

    @Column(name = "animal_reg_no")
    private String animalRegNo;

    @Column(name = "breed", nullable = false)
    private String breed;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "neutered", nullable = false)
    private String neutered;

    @Column(name = "age_weight", nullable = false)
    private String ageWeight;

    @Column(name = "rescue_characteristics", columnDefinition = "TEXT")
    private String rescueCharacteristics;

    @Column(name = "social_characteristics", columnDefinition = "TEXT")
    private String socialCharacteristics;

    @Column(name = "health_characteristics", columnDefinition = "TEXT")
    private String healthCharacteristics;

    @Column(name = "health_checkup", columnDefinition = "TEXT")
    private String healthCheckup;

    @Column(name = "vaccination_status", columnDefinition = "TEXT")
    private String vaccinationStatus;

    @Column(name = "occurrence_place")
    private String occurrencePlace;

    @Column(name = "reception_datetime", nullable = false)
    private LocalDateTime receptionDatetime;

    @Column(name = "other_notes", columnDefinition = "TEXT")
    private String otherNotes;

    @Column(name = "jurisdiction")
    private String jurisdiction;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "shelter_name")
    private String shelterName;

    @Column(name = "shelter_contact")
    private String shelterContact;

    @Column(name = "protection_location")
    private String protectionLocation;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "adoption_procedure", columnDefinition = "TEXT")
    private String adoptionProcedure;

    @Column(name = "adoption_support", columnDefinition = "TEXT")
    private String adoptionSupport;

    @Column(name = "volunteer_info", columnDefinition = "TEXT")
    private String volunteerInfo;

    @Column(name = "event_info", columnDefinition = "TEXT")
    private String eventInfo;

    @Column(name = "image_urls", columnDefinition = "TEXT")
    private String imageUrls;
}
