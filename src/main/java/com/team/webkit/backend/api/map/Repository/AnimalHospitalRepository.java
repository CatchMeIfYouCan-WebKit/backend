package com.team.webkit.backend.api.map.Repository;

import com.team.webkit.backend.api.map.entity.AnimalHospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalHospitalRepository extends JpaRepository<AnimalHospital, Long> {
    List<AnimalHospital> findAll();
}
