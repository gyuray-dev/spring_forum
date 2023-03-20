package dev.gyuray.forum.service;

import dev.gyuray.forum.domain.Comment;
import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.domain.Role;
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
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional
    public Long addComment(CommentForm commentForm, User user) {
        Comment comment = new Comment(commentForm.getContent());

        Long postId = commentForm.getPostId();
        Post foundPost = postService.findPostById(postId);
        comment.addToPost(foundPost);

        Long userId = user.getId();
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
            String formattedRegDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(regDate);
            commentListDTO.setFormattedRegDate(formattedRegDate);
        }

        return commentListDTOs;
    }

    @Transactional
    public void updateComment(Long commentId, CommentUpdateDTO commentUpdateDTO, User user) {
        Comment foundComment = findCommentById(commentId);

        if (foundComment.getUser().getId() == user.getId()) {
            foundComment.updateComment(commentUpdateDTO);
        } else {
            log.info("foundComment.getUser().getLoginId() = {}", foundComment.getUser().getLoginId());
            log.info("user = {}", user.getLoginId());
            throw new IllegalStateException("댓글을 수정할 권한이 없습니다.");
        }
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment foundComment = findCommentById(commentId);

        if (foundComment.getUser().getId() == user.getId() || user.getRole() == Role.ADMIN) {
            commentRepository.delete(commentId);
        } else {
            throw new IllegalStateException("댓글을 삭제할 권한이 없습니다.");
        }
    }

}
