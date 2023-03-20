package dev.gyuray.forum.repository.post;

import dev.gyuray.forum.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public Post save(Post post) {
        em.persist(post);
        return post;
    }
    public Optional<Post> findOne(Long postId) {
        return Optional.ofNullable(em.find(Post.class, postId));
    }

    public List<PostListDTO> findAll(int offset, int limit) {
        String query = "select new dev.gyuray.forum.repository.post.PostListDTO(" +
                "p.id, " +
                "p.title, " +
                "u.name, " +
                "p.regDate, " +
                "p.view, " +
                "''" +
                ") " +
                "from Post p " +
                "join p.user u " +
                "order by p.id desc";

        return em.createQuery(query, PostListDTO.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long totalCount() {
        return em.createQuery("select count(p) from Post p", Long.class)
                .getSingleResult();
    }

    public void delete(Long postId) {
        Post foundPost = em.find(Post.class, postId);
        em.remove(foundPost);
    }
}
