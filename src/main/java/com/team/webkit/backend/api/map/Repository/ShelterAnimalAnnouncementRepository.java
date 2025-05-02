package com.team.webkit.backend.api.map.Repository;

import com.team.webkit.backend.api.map.entity.ShelterAnimalAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelterAnimalAnnouncementRepository extends JpaRepository<ShelterAnimalAnnouncement, Long> {

    // 보호소 이름 기준 중복 제거
    @Query("SELECT DISTINCT s.shelterName, s.address, s.phone FROM ShelterAnimalAnnouncement s")
    List<Object[]> findDistinctShelters();
}
