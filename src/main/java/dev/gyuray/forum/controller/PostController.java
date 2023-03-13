package dev.gyuray.forum.controller;

import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.repository.comment.CommentListDTO;
import dev.gyuray.forum.repository.post.PostListDTO;
import dev.gyuray.forum.service.CommentService;
import dev.gyuray.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/posts")
    public String postList(
            @RequestParam(required = false) Integer currentPage,
            @CookieValue(required = false) Integer pageSize,
            Model model,
            HttpServletResponse response
    ) {
        if (currentPage == null || currentPage < 1) {
            currentPage = 1;
        }

        if (pageSize == null || (pageSize !=10 && pageSize != 30 && pageSize != 50)) {
            response.addCookie(new Cookie("pageSize", "10"));
            pageSize = 10;
            currentPage = 1;
        }

        Long totalPostCount = postService.getTotalCount();
        Integer lastPage = (int) Math.ceil(totalPostCount / (double) pageSize);
        currentPage = Math.min(currentPage, lastPage);

        List<PostListDTO> postListDTOs = postService.findAll(currentPage, pageSize);

        Integer startPage = (currentPage - 1) / 5 * 5 + 1;
        Integer endPage = Math.min(startPage + 4, lastPage);

        boolean hasNextPages = currentPage < (lastPage - 1) / 5 * 5 + 1;

        // for listing
        model.addAttribute("postListDTOs", postListDTOs);

        // for paging
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("hasNextPages", hasNextPages);

        // for pageSize radio button
        model.addAttribute("pageSize", pageSize);

        return "posts/postList";
    }

    @GetMapping("/posts/{postId}")
    public String viewPost(
            @PathVariable Long postId,
            @RequestParam(required = false) Long currentPage,
            Model model
    ) {
        // 게시글 로딩
        Post foundPost = postService.findPostById(postId);
        model.addAttribute("post", foundPost);


        // 댓글 로딩
        List<CommentListDTO> commentListDTOs = commentService.findAllByPostId(postId);
        model.addAttribute("commentListDTOs", commentListDTOs);

        // 현재 페이지 저장
        currentPage = (currentPage == null) ? 1 : currentPage;
        model.addAttribute("currentPage", currentPage);

        return "posts/postView";
    }
}
