package dev.gyuray.forum.repository.comment;


import dev.gyuray.forum.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public Comment save(Comment comment) {
        em.persist(comment);
        return comment;
    }
    public Optional<Comment> findOne(Long commentId) {
        return Optional.ofNullable(em.find(Comment.class, commentId));
    }

    public void update(CommentUpdateDTO commentDTO) {
        Comment foundComment = em.find(Comment.class, commentDTO.getId());
        foundComment.updateComment(commentDTO);
    }

    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c")
                .getResultList();
    }

    public void delete(Long commentId) {
        Comment foundComment = em.find(Comment.class, commentId);
        em.remove(foundComment);
    }
}
