package dev.gyuray.forum.repository.post;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostUpdateDTO {
    private Long id;
    private String title;
    private String content;
}
