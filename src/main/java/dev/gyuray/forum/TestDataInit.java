package dev.gyuray.forum;

import dev.gyuray.forum.repository.post.PostForm;
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

@RequiredArgsConstructor
@Profile("local")
@Component
@Slf4j
public class TestDataInit {

    private final UserService userService;
    private final PostService postService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    void initTestData() {
        log.info("Test Data Initialization");

        UserForm userForm = new UserForm();
        userForm.setName("운영자");
        userForm.setCity("cityA");
        userForm.setStreet("streetA");
        userForm.setZipcode("12345");
        userForm.setEmail("userA@email.com");

        Long userId = userService.join(userForm);

        for (int i = 1; i <= 120; i++) {
            PostForm postForm = new PostForm();
            postForm.setUserId(userId);
            postForm.setTitle("제목" + i);
            postForm.setContent("내용" + i);

            postService.post(postForm);
        }
    }
}
