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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final UploadFileRepository uploadFileRepository;

    @Value("${file.path}")
    private String filePath;

    @GetMapping("/{uploadFileId}")
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
}
