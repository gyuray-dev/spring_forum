package dev.gyuray.forum.service;

import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.domain.User;
import dev.gyuray.forum.repository.post.PostForm;
import dev.gyuray.forum.repository.post.PostListDTO;
import dev.gyuray.forum.repository.post.PostRepository;
import dev.gyuray.forum.repository.post.PostUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Transactional
    public Long addPost(PostForm postForm) {
        Post post = new Post();
        User writer = userService.findUser(postForm.getUserId());
        post.setUser(writer);
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        postRepository.save(post);
        return post.getId();
    }

    public Post findPostById(Long postId) {
        return postRepository.findOne(postId).orElseThrow(() -> {
                    throw new IllegalStateException("해당 게시글이 존재하지 않습니다");
                });
    }

    public List<PostListDTO> findAll(int pageNum, int pageSize) {
        int firstIndex = (pageNum - 1) * pageSize;
        List<PostListDTO> postListDTOs = postRepository.findAll(firstIndex, pageSize);

        // 날짜 포매팅
        for (PostListDTO postListDTO : postListDTOs) {
            LocalDateTime regDate = postListDTO.getRegDate();
            LocalDateTime now = LocalDateTime.now();
            long diff = ChronoUnit.HOURS.between(regDate, now);

            if (diff < 24) {
                postListDTO.setFormattedRegDate(DateTimeFormatter.ofPattern("HH:mm").format(regDate));
            } else {
                postListDTO.setFormattedRegDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(regDate));
            }
        }

        return postListDTOs;
    }

    public Long getTotalCount() {
        return postRepository.totalCount();
    }

    @Transactional
    public void updatePost(PostUpdateDTO postUpdateDTO) {
        Optional<Post> foundPost = postRepository.findOne(postUpdateDTO.getId());

        Post post = foundPost.orElseThrow(() -> {
            throw new IllegalStateException("삭제된 게시물입니다.");
        });

        post.setTitle(postUpdateDTO.getTitle());
        post.setContent(postUpdateDTO.getContent());
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.delete(postId);
    }

}


