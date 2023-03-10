package dev.gyuray.forum.repository.post;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostForm {
    private Long userId;
    private String title;
    private String content;
}
