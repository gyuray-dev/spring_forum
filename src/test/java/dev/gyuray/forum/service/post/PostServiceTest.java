package dev.gyuray.forum.service.post;

import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.repository.post.PostForm;
import dev.gyuray.forum.repository.post.PostUpdateDTO;
import dev.gyuray.forum.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;

    @Test
    void basicCrud() {
        //new post
        PostForm postForm = new PostForm();
        postForm.setTitle("제목1");
        postForm.setContent("내용1");

        Long postId = postService.post(postForm);
        Post foundPost = postService.findPostById(postId);

        Assertions.assertEquals(postForm.getTitle(), foundPost.getTitle());
        Assertions.assertEquals(postForm.getContent(), foundPost.getContent());

        //update
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO();
        postUpdateDTO.setId(postId);
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
}