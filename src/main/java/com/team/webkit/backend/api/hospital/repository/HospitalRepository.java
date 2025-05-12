package com.team.webkit.backend.api.hospital.repository;

import com.team.webkit.backend.api.hospital.entity.Hospital;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Integer> {

    List<Hospital> findByNameContaining(String name);

    List<Hospital> findByAddressContaining(String address);

    List<Hospital> findByNameContainingAndAddressContaining(String name, String address);

}
