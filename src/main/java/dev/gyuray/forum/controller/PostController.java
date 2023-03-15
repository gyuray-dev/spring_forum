package dev.gyuray.forum.controller;

import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.repository.comment.CommentListDTO;
import dev.gyuray.forum.repository.post.PostForm;
import dev.gyuray.forum.repository.post.PostListDTO;
import dev.gyuray.forum.repository.post.PostUpdateDTO;
import dev.gyuray.forum.service.CommentService;
import dev.gyuray.forum.service.PostPagerDTO;
import dev.gyuray.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        List<PostListDTO> postListDTOs = postService.findAll(currentPage, pageSize);
        model.addAttribute("postListDTOs", postListDTOs);

        PostPagerDTO postPagerDTO = postService.getPager(currentPage, pageSize);
        model.addAttribute("postPagerDTO", postPagerDTO);

        return "posts/postList";
    }

    @PostMapping("/posts")
    public String createPost(
            @ModelAttribute PostForm postForm
    ) {
        postService.addPost(postForm);
        log.info("postForm.getContent() = {}", postForm.getContent());

        return "redirect:/posts";
    }

    @GetMapping("/posts/{postId}")
    public String viewPost(
            @PathVariable Long postId,
            @RequestParam(required = false) Long currentPage,
            Model model
    ) {
        Post foundPost = postService.findPostById(postId);
        postService.addView(foundPost);
        model.addAttribute("post", foundPost);

        List<CommentListDTO> commentListDTOs = commentService.findAllByPostId(postId);
        model.addAttribute("commentListDTOs", commentListDTOs);

        currentPage = (currentPage == null) ? 1 : currentPage;
        model.addAttribute("currentPage", currentPage);

        return "posts/postView";
    }

    @GetMapping("/post")
    public String createForm() {
        return "posts/postForm";
    }

    @GetMapping("/posts/{postId}/edit")
    public String updatePostForm(
            @PathVariable Long postId,
            Model model
    ) {
        Post foundPost = postService.findPostById(postId);
        model.addAttribute("post", foundPost);
        return "posts/postUpdateForm";
    }

    @PostMapping("/posts/{postId}/edit")
    public String updatePost(
            @ModelAttribute PostUpdateDTO postUpdateDTO
    ) {
        postService.updatePost(postUpdateDTO);
        return "redirect:/posts/" + postUpdateDTO.getPostId();
    }

    @GetMapping("/posts/{postId}/delete")
    public String deletePost(
            @PathVariable Long postId
    ) {
        postService.deletePost(postId);
        return "redirect:/posts/";
    }
}
