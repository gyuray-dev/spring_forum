package dev.gyuray.forum.repository.user;

import dev.gyuray.forum.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public User save(User user) {
        em.persist(user);
        return user;
    }
    public Optional<User> findOne(Long userId) {
        return Optional.ofNullable(em.find(User.class, userId));
    }

    public void update(UserUpdateDTO userUpdateDTO) {
        User user = em.find(User.class, userUpdateDTO.getId());
        user.setAddress(userUpdateDTO.getAddress());
        user.setRole(userUpdateDTO.getRole());
        user.setPosts(userUpdateDTO.getPosts());
        user.setComments(userUpdateDTO.getComments());
    }

    public Optional<User> findByName(String name) {
        return Optional.ofNullable(
                em.createQuery("select u from User u where user_name = :name", User.class)
                .setParameter("name", name)
                .getSingleResult());
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u")
                .getResultList();
    }

    public void delete(Long userId) {
        User foundUser = em.find(User.class, userId);
        em.remove(foundUser);
    }

}