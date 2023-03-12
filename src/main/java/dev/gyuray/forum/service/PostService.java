package dev.gyuray.forum.service;

import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.domain.User;
import dev.gyuray.forum.repository.post.PostForm;
import dev.gyuray.forum.repository.post.PostRepository;
import dev.gyuray.forum.repository.post.PostUpdateDTO;
import dev.gyuray.forum.repository.post.PostListDTO;
import dev.gyuray.forum.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long post(PostForm postForm) {
        Post post = new Post();
        Long writerId = postForm.getUserId();
        User writer = userRepository.findOne(writerId).orElseThrow(IllegalStateException::new);
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
        return postRepository.findAll(firstIndex, pageSize);
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


