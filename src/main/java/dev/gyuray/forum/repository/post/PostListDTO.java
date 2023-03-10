package dev.gyuray.forum.repository.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter @Setter
public class PostListDTO {
    private Long postId;
    private String userName;
//    private Integer commentCount;
    private String title;
    private LocalDateTime regDate;
    private Integer view;
}
