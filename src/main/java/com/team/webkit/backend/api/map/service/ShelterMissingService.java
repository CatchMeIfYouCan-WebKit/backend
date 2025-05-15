package com.team.webkit.backend.api.map.service;

import com.team.webkit.backend.api.map.Repository.ShelterMissingRepository;
import com.team.webkit.backend.api.map.entity.ShelterMissing;
import com.team.webkit.backend.support.Coordinates;
import com.team.webkit.backend.support.KakaoApiClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShelterMissingService {

    private final ShelterMissingRepository shelterMissingRepository;
    private final KakaoApiClient kakaoApiClient;

    // 보호소(실종) 전체조회
    public List<ShelterMissing> getAllMissingAnimals() {
        log.info("모든 실종 동물 조회 요청");
        return shelterMissingRepository.findAll();
    }

    // 보호소(실종) 상세조회
    public ShelterMissing getMissingAnimal(Integer id) {
        log.info("실종 동물 조회 요청: id={}", id);
        return shelterMissingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("보호소(실종)를 찾을 수 없습니다."));
    }

    // 위도, 경도 구하기
    public void convertCoordinates() {
        List<ShelterMissing> animals = shelterMissingRepository.findAll();

        for (ShelterMissing animal : animals) {
            // 주소가 null이면 넘어가기
            if (animal.getAddress() == null) {
                continue;
            }

            if (animal.getLatitude() == null || animal.getLongitude() == null) {
                log.info("[좌표 변환 시작] 동물 ID: {}, 보호소명: {}, 주소: {}",
                    animal.getId(), animal.getShelterName(), animal.getAddress());

                Coordinates coords = kakaoApiClient.getCoordinatesWithFallback(
                    animal.getAddress());

                if (coords != null) {
                    animal.setLatitude(coords.getLatitude());
                    animal.setLongitude(coords.getLongitude());
                    shelterMissingRepository.save(animal);

                    log.info("[좌표 변환 성공] 동물 ID: {}, 위도: {}, 경도: {}",
                        animal.getId(), coords.getLatitude(), coords.getLongitude());
                } else {
                    log.warn("[좌표 변환 실패] 동물 ID: {}, 주소: {}",
                        animal.getId(), animal.getAddress());
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
