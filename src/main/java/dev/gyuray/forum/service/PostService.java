package dev.gyuray.forum.service;

import dev.gyuray.forum.controller.FileManager;
import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.domain.Role;
import dev.gyuray.forum.domain.UploadFile;
import dev.gyuray.forum.domain.User;
import dev.gyuray.forum.repository.post.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final FileManager fileManager;
    private final UploadFileRepository uploadFileRepository;

    @Transactional
    public Long addPost(PostForm postForm, User writer, String parentTreePath) throws IOException {
        Post post = new Post();
        post.setUser(writer);
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());

        List<MultipartFile> multipartFiles = postForm.getUploadFiles();
        if (multipartFiles != null) {
            List<UploadFile> uploadFiles = fileManager.storeFiles(multipartFiles);
            for (UploadFile uploadFile : uploadFiles) {
                uploadFile.addToPost(post);
            }
        }

        postRepository.save(post);

        // tree-path 지정
        if (parentTreePath == null) {
            parentTreePath = "/";
        } else {
            parentTreePath += "/";
        }
        post.setTreePath(parentTreePath + post.getId());
        String treePath = post.getTreePath();
        String _root = treePath.split("/")[1];
        post.setRoot(Long.parseLong(_root));

        return post.getId();
    }

    public Post findPostById(Long postId) {
        return postRepository.findOne(postId).orElseThrow(() -> {
            throw new IllegalStateException("해당 게시글이 존재하지 않습니다");
        });
    }

    public List<PostListDTO> findAll(int pageNum, int pageSize, PostSearchDTO postSearchDTO) {
        int firstIndex = (pageNum - 1) * pageSize;
        List<PostListDTO> postListDTOs = postRepository.findAll(firstIndex, pageSize, postSearchDTO);

        // 날짜 포매팅 및 들여쓰기 처리
        for (PostListDTO postListDTO : postListDTOs) {
            //날짜 포매팅
            LocalDateTime regDate = postListDTO.getRegDate();
            LocalDateTime now = LocalDateTime.now();
            long diff = ChronoUnit.HOURS.between(regDate, now);

            if (diff < 24) {
                postListDTO.setFormattedRegDate(DateTimeFormatter.ofPattern("HH:mm").format(regDate));
            } else {
                postListDTO.setFormattedRegDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(regDate));
            }

            //들여쓰기
            long depth = postListDTO.getTreePath().chars().filter(c -> c == '/').count();
            StringBuilder newTitle = new StringBuilder();
            if (depth >= 1) {
                for (int i = 0; i < depth; i++) {
                    newTitle.append("      ");
                }
                newTitle.append("ㄴ ");
                newTitle.append(postListDTO.getTitle());
                postListDTO.setTitle(newTitle.toString());
            }
        }

        return postListDTOs;
    }

    @Transactional
    public void addView(Post post, User user) {
        if (post.getUser().getId() != user.getId()) {
            post.setView(post.getView() + 1);
        }
    }

    public Long getTotalCount() {
        return postRepository.totalCount();
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateDTO postUpdateDTO, User user) throws IOException {
        Post post = findPostById(postId);

        if (post.getUser().getId() != user.getId()) {
            throw new IllegalStateException("게시글을 수정할 권한이 없습니다.");
        }

        // 첨부파일 삭제
        if (postUpdateDTO.getDeleteFileIds() != null) {
            for (Long deleteFileId : postUpdateDTO.getDeleteFileIds()) {
                UploadFile uploadFile = uploadFileRepository.findOne(deleteFileId).orElseThrow(() -> {
                    throw new IllegalStateException("존재하지 않는 파일입니다.");
                });

                Long userId = uploadFile.getPost().getUser().getId();

                if (userId == user.getId()) {
                    uploadFileRepository.delete(deleteFileId);
                    fileManager.deleteFile(uploadFile);
                }
            }
        }

        // 첨부파일 추가
        List<MultipartFile> multipartFiles = postUpdateDTO.getUploadFiles();

        if (multipartFiles != null) {
            List<UploadFile> uploadFiles = post.getUploadFiles();
            for (UploadFile newUploadFile : fileManager.storeFiles(multipartFiles)) {
                newUploadFile.addToPost(post);
            }

            post.setUploadFiles(uploadFiles);
        }

        post.setTitle(postUpdateDTO.getTitle());
        post.setContent(postUpdateDTO.getContent());
    }

    @Transactional
    public void deletePost(Long postId, User user) throws IOException {
        Post post = findPostById(postId);

        if (post.getUser().getId() == user.getId() || user.getRole() == Role.ADMIN) {
            fileManager.deleteFiles(post.getUploadFiles());
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


