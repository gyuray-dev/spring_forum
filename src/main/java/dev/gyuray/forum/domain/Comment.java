package dev.gyuray.forum.domain;

import dev.gyuray.forum.repository.comment.CommentUpdateDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private LocalDateTime regDate;

    private String content;

    private Long root;
    private String treePath;

    public Comment(String content) {
        this();
        this.content = content;
    }

    public Comment() {
        regDate = LocalDateTime.now();
    }

    public void updateComment(CommentUpdateDTO commentUpdateDTO) {
        this.content = commentUpdateDTO.getContent();
    }

    public void addToPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }

    public void addToUser(User user) {
        this.user = user;
        post.getComments().add(this);
    }

    private void setPost() {}
}
