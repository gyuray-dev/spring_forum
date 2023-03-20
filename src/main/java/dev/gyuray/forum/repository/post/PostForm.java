package dev.gyuray.forum.repository.post;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class PostForm {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
