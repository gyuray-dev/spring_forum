package dev.gyuray.forum.repository.post;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostSearchDTO {
    private String query;
    private String queryType;
}
