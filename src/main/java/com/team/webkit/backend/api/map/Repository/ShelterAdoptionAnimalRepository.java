package com.team.webkit.backend.api.map.Repository;

import com.team.webkit.backend.api.map.entity.ShelterAdoptionAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterAdoptionAnimalRepository extends JpaRepository<ShelterAdoptionAnimal, Long> {
    // 입양 관련 보호소도 따로 쿼리 가능
}
