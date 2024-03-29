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

    public List<CommentListDTO> findAll(Long postId) {
        String query = "select new dev.gyuray.forum.repository.comment.CommentListDTO(" +
                "u.name, " +
                "c.regDate, " +
                "c.content, " +
                "'', " +
                "c.id, " +
                "u.id, " +
                "c.treePath, " +
                "0" +
                ") " +
                "from Comment c " +
                "join c.user u " +
                "where c.post.id = :postId " +
                "order by c.root, c.treePath";

        return em.createQuery(query, CommentListDTO.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public void delete(Long commentId) {
        Comment foundComment = em.find(Comment.class, commentId);
        em.remove(foundComment);
    }
}
