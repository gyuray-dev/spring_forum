package dev.gyuray.forum.controller;

import dev.gyuray.forum.domain.UploadFile;
import dev.gyuray.forum.repository.post.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final UploadFileRepository uploadFileRepository;
    private final FileManager fileManager;

    @Value("${file.path}")
    private String filePath;

    @GetMapping("/attachment/{uploadFileId}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long uploadFileId
    ) throws MalformedURLException {
        UploadFile uploadFile = uploadFileRepository.findOne(uploadFileId).orElseThrow(() -> {
            throw new IllegalArgumentException("해당 파일이 존재하지 않습니다.");
        });

        String storedFileName = uploadFile.getStoredFileName();
        String originalFileName = uploadFile.getOriginalFileName();

        UrlResource resource = new UrlResource("file:" + filePath + storedFileName);
        String encodedOriginalFileName = UriUtils.encode(originalFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedOriginalFileName + "\"";


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    @ResponseBody
    @GetMapping("/images/{storedFileName}")
    public Resource downloadImage(
            @PathVariable String storedFileName
    ) throws MalformedURLException {
        return new UrlResource("file:" + filePath + storedFileName);
    }

    @ResponseBody
    @PostMapping("/images/new")
    public String uploadImage(
            @RequestParam("image") MultipartFile multipartFile
    ) throws IOException {

        if (multipartFile.isEmpty()) {
            return "not found";
        }

        UploadFile uploadFile = fileManager.storeFile(multipartFile);
        String storedFileName = uploadFile.getStoredFileName();

        return "/files/images/" + storedFileName;
    }
}
