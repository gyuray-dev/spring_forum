package dev.gyuray.forum.service;

import dev.gyuray.forum.domain.Comment;
import dev.gyuray.forum.domain.Role;
import dev.gyuray.forum.domain.User;
import dev.gyuray.forum.repository.comment.CommentForm;
import dev.gyuray.forum.repository.comment.CommentUpdateDTO;
import dev.gyuray.forum.repository.post.PostForm;
import dev.gyuray.forum.repository.user.UserForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired CommentService commentService;
    @Autowired UserService userService;
    @Autowired PostService postService;

    @Test
    void basicCrud() {
        // writer
        UserForm userFormA = new UserForm();
        userFormA.setLoginId("admin");
        userFormA.setPassword("1234");
        Long userId = userService.join(userFormA);
        User user = userService.findUser(userId);
        user.setRole(Role.ADMIN);

        //new post
        PostForm postForm = new PostForm();
        postForm.setTitle("제목1");
        postForm.setContent("내용1");

        Long postId = postService.addPost(postForm, user);

        //new comment
        CommentForm commentForm = new CommentForm();
        commentForm.setContent("댓글 내용1");
        commentForm.setPostId(postId);

        Long commentId = commentService.addComment(commentForm, user);
        Comment foundComment = commentService.findCommentById(commentId);

        Assertions.assertEquals(commentForm.getContent(), foundComment.getContent());
        assertEquals(commentForm.getContent(), foundComment.getContent());

        //update
        CommentUpdateDTO commentUpdateDTO = new CommentUpdateDTO();
        commentUpdateDTO.setContent("새 내용");
        commentService.updateComment(commentId, commentUpdateDTO ,user);

        Assertions.assertEquals(commentUpdateDTO.getContent(), foundComment.getContent());
        assertEquals(commentUpdateDTO.getContent(), foundComment.getContent());

        //delete
        commentService.deleteComment(commentId, user);
        assertThrows(IllegalStateException.class, () -> {
            commentService.findCommentById(commentId);
        });
    }

}