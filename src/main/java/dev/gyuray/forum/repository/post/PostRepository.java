package dev.gyuray.forum.repository.post;

import dev.gyuray.forum.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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

    public List<PostListDTO> findAll(int offset, int limit, PostSearchDTO postSearchDTO) {
        String jpql = "select new dev.gyuray.forum.repository.post.PostListDTO(" +
                "p.id, " +
                "p.title, " +
                "u.name, " +
                "p.regDate, " +
                "p.view, " +
                "'', " +
                "(select count(*) from Comment c where c.post = p)" +
                ") " +
                "from Post p " +
                "join p.user u ";

        String searchQuery = "";
        String searchQueryType = "";

        boolean isSearching = false;
        if (postSearchDTO != null && postSearchDTO.getQueryType() != null &&postSearchDTO.getQuery() != null) {
            searchQueryType = postSearchDTO.getQueryType(); //p.title, p.content, u.name
            searchQuery = postSearchDTO.getQuery();

            if (searchQueryType.equals("user")) {
                jpql += "where u.name like :query ";
            } else {
                jpql += "where p." + searchQueryType + " like :query ";
            }

            isSearching = true;
        }

        jpql += "order by p.id desc";

        TypedQuery<PostListDTO> query = em.createQuery(jpql, PostListDTO.class);

        if (isSearching) {
            query.setParameter("query", "%" + searchQuery + "%");
        }

        return query
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
