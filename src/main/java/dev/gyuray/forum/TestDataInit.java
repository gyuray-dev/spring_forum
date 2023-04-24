package dev.gyuray.forum;

import dev.gyuray.forum.domain.Role;
import dev.gyuray.forum.domain.User;
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

import java.io.IOException;
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
    public void initTestData() throws IOException {
        log.info("Test Data Initialization");

        UserForm userForm = new UserForm();
        userForm.setName("어드민");
        userForm.setCity("cityA");
        userForm.setStreet("streetA");
        userForm.setZipcode("12345");
        userForm.setEmail("userA@email.com");
        userForm.setLoginId("admin");
        userForm.setPassword("123!@#");
        Long userId1 = userService.join(userForm);
        User user1 = userService.findUser(userId1);
        user1.setRole(Role.ADMIN);

        userForm.setName("홍길동");
        userForm.setLoginId("hong");
        userForm.setPassword("1234");
        userForm.setEmail("userB@email.com");
        Long userId2 = userService.join(userForm);
        User user2 = userService.findUser(userId2);

        StringBuilder parentTreePath = new StringBuilder("");
        for (int i = 1; i <= 9; i++) {
            PostForm postForm = new PostForm();
            postForm.setTitle("제목" + i);
            postForm.setContent("내용" + i);
            postForm.setParentTreePath(parentTreePath.toString());

            Long postId = postService.addPost(postForm, i % 2 != 0? user1 : user2);
            String hexaTriDecimalId = String.format("%6s", Long.toString(postId, Character.MAX_RADIX))
                    .replace(" ", "0");
            parentTreePath.append("/" + hexaTriDecimalId);
        }

        List<PostListDTO> postListDTOs = postService.findAll(1, 10, null);
        for (int i = 0; i < postListDTOs.size(); i++) {
            for (int j = 1; j <= i + 1; j++) {
                CommentForm commentForm = new CommentForm();
                commentForm.setContent("댓글 " + j);
                if (j % 2 != 0) {
                    commentService.addComment(commentForm, postListDTOs.get(i).getPostId(), user1);
                } else {
                    commentService.addComment(commentForm, postListDTOs.get(i).getPostId(), user2);
                }
            }
        }

        log.info("Initialization complete");
    }
}
