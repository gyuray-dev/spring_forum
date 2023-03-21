package dev.gyuray.forum.repository.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter @Setter
public class PostForm {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private List<MultipartFile> uploadFiles;
}
