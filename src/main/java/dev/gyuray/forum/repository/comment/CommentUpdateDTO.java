package dev.gyuray.forum.repository.comment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentUpdateDTO {
    private Long commentId;
    private String content;
}
