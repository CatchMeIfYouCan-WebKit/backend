package com.team.webkit.backend.support;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class KakaoApiClient {

    @Value("${kakao.rest-api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Coordinates getCoordinatesWithFallback(String address) {
        // 전처리된 주소 출력
        String cleanedAddress = preprocessAddress(address);
        log.info("전처리된 주소: {}", cleanedAddress);

        log.info("카카오 API 호출 전 주소: {}", cleanedAddress);
        Coordinates coords = getCoordinates(cleanedAddress);

        if (coords == null) {
            // 마지막으로 구/시까지만 시도 (ex: "광주광역시 북구")
            String[] parts = address.split(" ");
            if (parts.length >= 2) {
                String fallbackAddress = parts[0] + " " + parts[1];
                coords = getCoordinates(fallbackAddress);

                log.info("Fallback 주소: {}", fallbackAddress);
            }
        }

        return coords;
    }

    public Coordinates getCoordinates(String address) {
        try {
            // 공백 정리
            String cleanedAddress = address.replaceAll("\\s+", " ").trim();

            String url =
                "https://dapi.kakao.com/v2/local/search/address.json?query=" + cleanedAddress;
            log.info("카카오 API 요청 URL: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + apiKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, JsonNode.class
            );

            log.info("카카오 API 응답 상태: {}", response.getStatusCode());
            log.info("카카오 API 응답 본문: {}", response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode documents = response.getBody().get("documents");
                if (documents != null && documents.isArray() && !documents.isEmpty()) {
                    JsonNode first = documents.get(0);
                    Double lat = first.get("y").asDouble();
                    Double lng = first.get("x").asDouble();
                    return new Coordinates(lat, lng);
                }
            }
        } catch (Exception e) {
            log.error("카카오 API 호출 중 오류 발생", e);
        }
        return null;
    }

    private String preprocessAddress(String address) {
        // 괄호 안 내용 제거
        String cleaned = address.replaceAll("\\(.*?\\)", "");

        // 한글, 숫자, 공백만 남기고 나머지 제거 (특수문자 제거)
        cleaned = cleaned.replaceAll("[^가-힣0-9\\s]", "");

        // 공백 여러 개 → 하나로 축소
        cleaned = cleaned.replaceAll("\\s+", " ").trim();

        return cleaned;
    }
}
