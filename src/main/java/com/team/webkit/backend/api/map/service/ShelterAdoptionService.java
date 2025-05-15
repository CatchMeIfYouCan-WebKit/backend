package com.team.webkit.backend.api.map.service;

import com.team.webkit.backend.api.map.Repository.ShelterAdoptionRepository;
import com.team.webkit.backend.api.map.entity.ShelterAdoption;
import com.team.webkit.backend.support.Coordinates;
import com.team.webkit.backend.support.KakaoApiClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShelterAdoptionService {

    private final ShelterAdoptionRepository shelterAdoptionRepository;
    private final KakaoApiClient kakaoApiClient;

    // 보호소(입양) 전체조회
    public List<ShelterAdoption> getAllAnimals() {
        log.info("모든 분양 동물 조회 요청");
        return shelterAdoptionRepository.findAll();
    }

    // 보호소(입양) 상세조회
    public ShelterAdoption getAnimal(Integer id) {
        log.info("분양 동물 조회 요청: id={}", id);
        return shelterAdoptionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("보호소(입양)를 찾을 수 없습니다."));
    }

    // 위도, 경도 구하기
    public void convertCoordinates() {
        List<ShelterAdoption> animals = shelterAdoptionRepository.findAll();

        for (ShelterAdoption animal : animals) {
            // 주소가 null이면 넘어가기
            if (animal.getProtectionLocation() == null) {
                continue;
            }

            if (animal.getLatitude() == null || animal.getLongitude() == null) {
                log.info("[좌표 변환 시작] 동물 ID: {}, 보호소명: {}, 보호 장소: {}",
                    animal.getId(), animal.getShelterName(), animal.getProtectionLocation());

                Coordinates coords = kakaoApiClient.getCoordinatesWithFallback(
                    animal.getProtectionLocation());

                if (coords != null) {
                    animal.setLatitude(coords.getLatitude());
                    animal.setLongitude(coords.getLongitude());
                    shelterAdoptionRepository.save(animal);

                    log.info("[좌표 변환 성공] 동물 ID: {}, 위도: {}, 경도: {}",
                        animal.getId(), coords.getLatitude(), coords.getLongitude());
                } else {
                    log.warn("[좌표 변환 실패] 동물 ID: {}, 보호 장소: {}",
                        animal.getId(), animal.getProtectionLocation());
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
