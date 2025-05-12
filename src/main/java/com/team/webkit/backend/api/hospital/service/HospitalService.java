package com.team.webkit.backend.api.hospital.service;

import com.team.webkit.backend.api.hospital.entity.Hospital;
import com.team.webkit.backend.api.hospital.repository.HospitalRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    // 전체 병원 조회
    public List<Hospital> findAll() {
        log.info("모든 병원 조회");
        return hospitalRepository.findAll();
    }

    // 병원 상세조회 : 이름, 지역
    public List<Hospital> searchByNameOrAddress(String name, String address) {
        log.info("searchByNameOrAddress called with name: {}, address: {}", name, address);

        if (name != null && address != null) {
            log.info("Searching by name and address");
            return hospitalRepository.findByNameContainingAndAddressContaining(name, address);
        } else if (name != null) {
            log.info("Searching by name only");
            return hospitalRepository.findByNameContaining(name);
        } else if (address != null) {
            log.info("Searching by address only");
            return hospitalRepository.findByAddressContaining(address);
        } else {
            log.info("No search parameters provided, returning empty list");
            return Collections.emptyList();
        }
    }


    // 병원 상세조회 : 아이디
    public Optional<Hospital> findById(Integer id) {
        log.info("병원 조회: id={}", id);
        return hospitalRepository.findById(id);
    }


}
