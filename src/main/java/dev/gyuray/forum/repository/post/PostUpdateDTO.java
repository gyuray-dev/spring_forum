package dev.gyuray.forum.repository.post;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostUpdateDTO {
    private String title;
    private String content;
}
