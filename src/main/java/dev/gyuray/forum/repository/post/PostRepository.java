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

    public void update(PostUpdateDTO postUpdateDTO) {
        Post foundPost = em.find(Post.class, postUpdateDTO.getId());
        foundPost.setId(postUpdateDTO.getId());
        foundPost.setTitle(postUpdateDTO.getTitle());
        foundPost.setContent(postUpdateDTO.getContent());
    }

    public List<Post> findAll() {
        return em.createQuery("select p from Post p")
                .getResultList();
    }

    public void delete(Long postId) {
        Post foundPost = em.find(Post.class, postId);
        em.remove(foundPost);
    }
}
