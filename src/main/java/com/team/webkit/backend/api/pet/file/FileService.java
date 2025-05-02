package com.team.webkit.backend.api.pet.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    public String save(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path savePath = Paths.get("uploads", fileName);
            Files.createDirectories(savePath.getParent());
            Files.copy(file.getInputStream(), savePath);

            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
