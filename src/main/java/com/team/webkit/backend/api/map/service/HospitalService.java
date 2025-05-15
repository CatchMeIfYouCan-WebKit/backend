package com.team.webkit.backend.api.map.service;

import com.team.webkit.backend.api.map.Repository.HospitalRepository;
import com.team.webkit.backend.api.map.entity.Hospital;
import com.team.webkit.backend.support.Coordinates;
import com.team.webkit.backend.support.KakaoApiClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final KakaoApiClient kakaoApiClient;

    // 병원 전체조회
    public List<Hospital> getAllHospitals() {
        log.info("모든 병원 조회 요청");
        return hospitalRepository.findAll();
    }

    // 병원 상세조회
    public Hospital getHospital(Integer id) {
        log.info("병원 조회 요청: id={}", id);
        return hospitalRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("병원을 찾을 수 없습니다."));
    }

    // 위도 경도 구하기
    public void convertCoordinates() {
        List<Hospital> hospitals = hospitalRepository.findAll();

        for (Hospital hospital : hospitals) {
            // 주소가 null이면 넘어가기
            if (hospital.getAddress() == null) {
                continue;
            }

            if (hospital.getLatitude() == null || hospital.getLongitude() == null) {
                log.info("[좌표 변환 시작] 병원 ID: {}, 병원명: {}, 주소: {}", hospital.getId(),
                    hospital.getName(), hospital.getAddress());

                Coordinates coords = kakaoApiClient.getCoordinatesWithFallback(
                    hospital.getAddress());

                if (coords != null) {
                    hospital.setLatitude(coords.getLatitude());
                    hospital.setLongitude(coords.getLongitude());
                    hospitalRepository.save(hospital);

                    log.info("[좌표 변환 성공] 위도: {}, 경도: {}", coords.getLatitude(),
                        coords.getLongitude());
                } else {
                    log.warn("[좌표 변환 실패] 병원명: {}, 주소: {}", hospital.getName(),
                        hospital.getAddress());
                }

                // 초당 20회 제한 (50ms 딜레이)
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        }

        log.info("[좌표 변환 종료] 전체 변환 작업 완료");
    }

}
