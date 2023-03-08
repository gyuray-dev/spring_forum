package dev.gyuray.forum.repository.post;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostUpdateDTO {
    Long id;
    String title;
    String content;
}
