package dev.gyuray.forum.repository.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class CommentForm {
    @NotBlank
    private String content;
}
