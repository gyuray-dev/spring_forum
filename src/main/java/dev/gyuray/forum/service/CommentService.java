package dev.gyuray.forum.service;

import dev.gyuray.forum.domain.Comment;
import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.domain.User;
import dev.gyuray.forum.repository.comment.CommentForm;
import dev.gyuray.forum.repository.comment.CommentListDTO;
import dev.gyuray.forum.repository.comment.CommentRepository;
import dev.gyuray.forum.repository.comment.CommentUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional
    public Long addComment(CommentForm commentForm) {
        Comment comment = new Comment(commentForm.getContent());

        Long postId = commentForm.getPostId();
        Post foundPost = postService.findPostById(postId);
        comment.addToPost(foundPost);

        Long userId = commentForm.getUserId();
        User foundUser = userService.findUser(userId);
        comment.addToUser(foundUser);

        commentRepository.save(comment);
        return comment.getId();
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findOne(commentId).orElseThrow(() -> {
            throw new IllegalStateException("해당 댓글이 존재하지 않습니다.");
        });
    }

    public List<CommentListDTO> findAllByPostId(Long postId) {
        List<CommentListDTO> commentListDTOs = commentRepository.findAll(postId);
        for (CommentListDTO commentListDTO : commentListDTOs) {
            LocalDateTime regDate = commentListDTO.getRegDate();
            String formattedRegDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(regDate);
            commentListDTO.setFormattedRegDate(formattedRegDate);
        }

        return commentListDTOs;
    }

    @Transactional
    public void updateComment(CommentUpdateDTO commentUpdateDTO) {
        commentRepository.update(commentUpdateDTO);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.delete(commentId);
    }

}
