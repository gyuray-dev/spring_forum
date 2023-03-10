package dev.gyuray.forum.service;

import dev.gyuray.forum.domain.Comment;
import dev.gyuray.forum.repository.comment.CommentForm;
import dev.gyuray.forum.repository.comment.CommentUpdateDTO;
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

    @Test
    void basicCrud() {
        //new comment
        CommentForm commentForm = new CommentForm();
        commentForm.setContent("댓글 내용1");

        Long commentId = commentService.comment(commentForm);
        Comment foundComment = commentService.findCommentById(commentId);

        Assertions.assertEquals(commentForm.getContent(), foundComment.getContent());
        assertEquals(commentForm.getContent(), foundComment.getContent());

        //update
        CommentUpdateDTO commentUpdateDTO = new CommentUpdateDTO();
        commentUpdateDTO.setId(commentId);
        commentUpdateDTO.setContent("새 내용");
        commentService.updateComment(commentUpdateDTO);

        Assertions.assertEquals(commentUpdateDTO.getContent(), foundComment.getContent());
        assertEquals(commentUpdateDTO.getContent(), foundComment.getContent());

        //delete
        commentService.deleteComment(commentId);
        assertThrows(IllegalStateException.class, () -> {
            commentService.findCommentById(commentId);
        });
    }

}