package dev.gyuray.forum.service;

import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.domain.User;
import dev.gyuray.forum.repository.post.PostForm;
import dev.gyuray.forum.repository.post.PostListDTO;
import dev.gyuray.forum.repository.post.PostUpdateDTO;
import dev.gyuray.forum.repository.user.UserForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    @Test
    void basicCrud() {
        //new post
        PostForm postForm = new PostForm();
        postForm.setTitle("제목1");
        postForm.setContent("내용1");

        // writer
        UserForm userFormA = new UserForm();
        userFormA.setLoginId("admin");
        userFormA.setPassword("1234");
        Long userId = userService.join(userFormA);
        User user = userService.findUser(userId);

        Long postId = postService.addPost(postForm, user);
        Post foundPost = postService.findPostById(postId);

        Assertions.assertEquals(postForm.getTitle(), foundPost.getTitle());
        Assertions.assertEquals(postForm.getContent(), foundPost.getContent());

        //update
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO();
        postUpdateDTO.setPostId(postId);
        postUpdateDTO.setTitle("새 제목");
        postUpdateDTO.setContent("새 내용");
        postService.updatePost(postUpdateDTO);

        Assertions.assertEquals(postUpdateDTO.getTitle(), foundPost.getTitle());
        Assertions.assertEquals(postUpdateDTO.getContent(), foundPost.getContent());

        //delete
        postService.deletePost(postId);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            postService.findPostById(postId);
        });
    }

    @Test
    void findAll() {
        // writer
        UserForm userFormA = new UserForm();
        userFormA.setLoginId("userA");
        userFormA.setPassword("1234");
        Long userId = userService.join(userFormA);
        User user = userService.findUser(userId);
        log.info("userId = {}", userId);

        // post
        PostForm postForm1 = new PostForm();
        postForm1.setTitle("제목1");
        PostForm postForm2 = new PostForm();
        postForm2.setTitle("제목2");

        Long postId1 = postService.addPost(postForm1, user);
        Long postId2 = postService.addPost(postForm2, user);
        List<PostListDTO> postListDTOs = postService.findAll(1, 10);

        Assertions.assertEquals(2, postListDTOs.size());
    }
}