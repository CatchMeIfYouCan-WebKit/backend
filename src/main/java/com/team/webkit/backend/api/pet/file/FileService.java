package com.team.webkit.backend.api.pet.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    public String save(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            System.out.println("빈 파일");
            throw new IllegalArgumentException("빈 파일입니다.");
        }

        try {
            String originalName = file.getOriginalFilename();
            System.out.println("[FileService] 원본 파일명: " + originalName);

            // 확장자 추출: 확장자 없을 경우 .jpg 기본값 사용
            String ext = Optional.ofNullable(file.getOriginalFilename())
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf(".")))
                .orElse(".jpg");

            // UUID로 안전한 파일명 생성
            String fileName = UUID.randomUUID() + ext;

            // 절대 경로에 저장
            Path savePath = Paths.get(System.getProperty("user.dir"), "uploads", fileName)
                .toAbsolutePath()
                .normalize();

            Files.createDirectories(savePath.getParent()); // 폴더 없으면 생성
            Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("[FileService] 파일 저장 완료");
            System.out.println("[FileService] 저장 경로: " + savePath);
            System.out.println("[FileService] 저장된 파일 크기: " + Files.size(savePath) + " bytes");

            // DB에는 상대 경로 저장
            return "/uploads/" + fileName;

        } catch (IOException e) {
            System.out.println("[FileService] 파일 저장 실패: " + e.getMessage());
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
