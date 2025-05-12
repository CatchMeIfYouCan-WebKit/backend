package com.team.webkit.backend.api.vet.repository;

import com.team.webkit.backend.api.vet.entity.Vet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VetRepository extends JpaRepository<Vet, Integer> {

    boolean existsByLoginId(String loginId);

    Optional<Vet> findByLoginId(String loginId);

    List<Vet> findByHospitalId(Integer hospitalId);
}
