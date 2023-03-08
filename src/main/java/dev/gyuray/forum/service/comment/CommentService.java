package dev.gyuray.forum.service.comment;

import dev.gyuray.forum.domain.Comment;
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

    public Long comment(CommentForm commentForm) {
        Comment comment = new Comment();
        comment.setContent(commentForm.getContent());
        commentRepository.save(comment);
        return comment.getId();
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
