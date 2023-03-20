package dev.gyuray.forum.service;

import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.domain.Role;
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
    public Long addPost(PostForm postForm, User writer) {
        Post post = new Post();
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

    @Transactional
    public void addView(Post post) {
        // TODO - 로그인 구현 후 본인 게시글은 조회수가 올라가지 않도록 변경
        post.setView(post.getView() + 1);
    }

    public Long getTotalCount() {
        return postRepository.totalCount();
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateDTO postUpdateDTO, User user) {
        Post post = findPostById(postId);

        if (post.getUser().getId() == user.getId()) {
            post.updatePost(postUpdateDTO);
        } else {
            throw new IllegalStateException("게시글을 수정할 권한이 없습니다.");
        }
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = findPostById(postId);

        if (post.getUser().getId() == user.getId() || user.getRole() == Role.ADMIN) {
            postRepository.delete(postId);
        } else {
            throw new IllegalStateException("게시글을 삭제할 권한이 없습니다.");
        }
    }


    public PostPagerDTO getPager(int currentPage, int pageSize) {
        Long totalPostCount = getTotalCount();
        int lastPage = Math.max(1, (int) Math.ceil(totalPostCount / (double) pageSize));
        currentPage = Math.min(currentPage, lastPage);

        int startPage = (currentPage - 1) / 5 * 5 + 1;
        int endPage = Math.min(startPage + 4, lastPage);

        boolean hasNextPages = currentPage < (lastPage - 1) / 5 * 5 + 1;

        PostPagerDTO postPagerDTO = new PostPagerDTO();
        postPagerDTO.setCurrentPage(currentPage);
        postPagerDTO.setStartPage(startPage);
        postPagerDTO.setEndPage(endPage);
        postPagerDTO.setLastPage(lastPage);
        postPagerDTO.setHasNextPages(hasNextPages);
        postPagerDTO.setPageSize(pageSize);

        return postPagerDTO;
    }
}


