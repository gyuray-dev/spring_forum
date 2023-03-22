package dev.gyuray.forum.controller;

import dev.gyuray.forum.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileManager {

    @Value("${file.path}")
    private String filePath;

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storedFiles = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storedFiles.add(storeFile(multipartFile));
            }
        }
        return storedFiles;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String storingFileName = UUID.randomUUID() + "." + ext;


        multipartFile.transferTo(new File(filePath + storingFileName));

        return new UploadFile(originalFilename, storingFileName, multipartFile.getSize());
    }

    public void deleteFiles(List<UploadFile> uploadFiles) throws IOException {
        for (UploadFile uploadFile : uploadFiles) {
            deleteFile(uploadFile);
        }
    }

    public void deleteFile(UploadFile uploadFile) throws IOException {
        File file = new File(filePath + uploadFile.getStoredFileName());
        Files.deleteIfExists(file.toPath());
    }
}
