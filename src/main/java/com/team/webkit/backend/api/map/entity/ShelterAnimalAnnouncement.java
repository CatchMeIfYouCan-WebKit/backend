package com.team.webkit.backend.api.map.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "shelter_animal_announcements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShelterAnimalAnnouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "announcement_no", nullable = false, unique = true)
    private String announcementNo;

    @Column(name = "animal_type", nullable = false)
    private String animalType;

    @Column(name = "breed", nullable = false)
    private String breed;

    @Column(name = "coat_color", nullable = false)
    private String coatColor;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "neutered", nullable = false)
    private String neutered;

    @Column(name = "characteristics", columnDefinition = "TEXT")
    private String characteristics;

    @Column(name = "rescue_date", nullable = false)
    private LocalDate rescueDate;

    @Column(name = "rescue_reason")
    private String rescueReason;

    @Column(name = "rescue_location", nullable = false)
    private String rescueLocation;

    @Column(name = "announce_start", nullable = false)
    private LocalDate announceStart;

    @Column(name = "announce_end", nullable = false)
    private LocalDate announceEnd;

    @Column(name = "shelter_name", nullable = false)
    private String shelterName;

    @Column(name = "representative", nullable = false)
    private String representative;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "image_url")
    private String imageUrl;
}
