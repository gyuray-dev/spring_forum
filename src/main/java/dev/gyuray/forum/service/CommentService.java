package dev.gyuray.forum.service;

import dev.gyuray.forum.domain.Comment;
import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.repository.comment.CommentForm;
import dev.gyuray.forum.repository.comment.CommentRepository;
import dev.gyuray.forum.repository.comment.CommentUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public Long comment(CommentForm commentForm) {
        Comment comment = new Comment(commentForm.getContent());

        Long postId = commentForm.getPostId();
        Post foundPost = postService.findPostById(postId);
        comment.addToPost(foundPost);

        commentRepository.save(comment);
        return comment.getId();
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findOne(commentId).orElseThrow(() -> {
            throw new IllegalStateException("해당 댓글이 존재하지 않습니다.");
        });
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
