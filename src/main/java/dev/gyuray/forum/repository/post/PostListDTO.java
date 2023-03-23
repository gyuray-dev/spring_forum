package dev.gyuray.forum.repository.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter @Setter
public class PostListDTO {
    private Long postId;
    private String title;
    private String userName;
    private LocalDateTime regDate;
    private Integer view;
    private String formattedRegDate;
    private Long commentsCount;
    private String treePath;
}
