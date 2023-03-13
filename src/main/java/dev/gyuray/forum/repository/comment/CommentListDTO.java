package dev.gyuray.forum.repository.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class CommentListDTO {
    private String userName;
    private LocalDateTime regDate;
    private String content;

    private String formattedRegDate;

    public CommentListDTO(String userName, LocalDateTime regDate, String content, String formattedRegDate) {
        this.userName = userName;
        this.regDate = regDate;
        this.content = content;
        this.formattedRegDate = formattedRegDate;
    }
}
