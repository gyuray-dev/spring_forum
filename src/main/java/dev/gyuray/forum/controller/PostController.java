package dev.gyuray.forum.controller;

import dev.gyuray.forum.repository.post.PostListDTO;
import dev.gyuray.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping("/posts/{pageNum}")
    public String postList(
            @PathVariable Integer pageNum,
            @CookieValue(required = false) Integer pageSize,
            Model model
    ) {
        if (pageSize == null) pageSize = 10;

        List<PostListDTO> postListDTOs = postService.findAll(pageNum, pageSize);
        model.addAttribute("postListDTOs", postListDTOs);

        Integer startPage = (pageNum - 1) / pageSize + 1;
        model.addAttribute("startPage", startPage);
        model.addAttribute("currentPage", pageNum);

        return "posts/postList";
    }
}
