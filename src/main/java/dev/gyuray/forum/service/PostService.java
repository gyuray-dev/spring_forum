package dev.gyuray.forum.service;

import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.repository.post.PostForm;
import dev.gyuray.forum.repository.post.PostRepository;
import dev.gyuray.forum.repository.post.PostUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long post(PostForm postForm) {
        Post post = new Post();
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


