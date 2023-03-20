package dev.gyuray.forum.repository.comment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentForm {
    private String content;
    private Long postId;
}
