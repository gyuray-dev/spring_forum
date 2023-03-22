package dev.gyuray.forum.repository.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class CommentListDTO {
    private String userName;
    private LocalDateTime regDate;
    private String content;

    private String formattedRegDate;
    private Long commentId;
    private Long userId;
}
