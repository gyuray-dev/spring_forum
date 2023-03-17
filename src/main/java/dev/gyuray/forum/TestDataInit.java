package dev.gyuray.forum;

import dev.gyuray.forum.repository.comment.CommentForm;
import dev.gyuray.forum.repository.post.PostForm;
import dev.gyuray.forum.repository.post.PostListDTO;
import dev.gyuray.forum.service.CommentService;
import dev.gyuray.forum.service.PostService;
import dev.gyuray.forum.repository.user.UserForm;
import dev.gyuray.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Profile("local")
@Component
@Slf4j
public class TestDataInit {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initTestData() {
        log.info("Test Data Initialization");

        UserForm userForm = new UserForm();
        userForm.setName("운영자");
        userForm.setCity("cityA");
        userForm.setStreet("streetA");
        userForm.setZipcode("12345");
        userForm.setEmail("userA@email.com");
        userForm.setLoginId("admin");
        userForm.setPassword("1234");

        Long userId = userService.join(userForm);

        for (int i = 1; i <= 15; i++) {
            PostForm postForm = new PostForm();
            postForm.setUserId(userId);
            postForm.setTitle("제목" + i);
            postForm.setContent("내용" + i);

            postService.addPost(postForm);
        }

        List<PostListDTO> postListDTOs = postService.findAll(1, 10);
        for (int i = 0; i < postListDTOs.size(); i++) {
            for (int j = 1; j <= i + 1; j++) {
                CommentForm commentForm = new CommentForm();
                commentForm.setContent("댓글 " + j);
                commentForm.setUserId(userId);
                commentForm.setPostId(postListDTOs.get(i).getPostId());
                commentService.addComment(commentForm);
            }
        }
    }
}
