package dev.gyuray.forum.repository.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class CommentUpdateDTO {
    @NotBlank
    private String content;
}
