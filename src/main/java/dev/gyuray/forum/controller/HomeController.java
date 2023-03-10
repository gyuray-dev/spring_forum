package dev.gyuray.forum.controller;

import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.repository.post.PostListDTO;
import dev.gyuray.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final PostService postService;
    
    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/posts/1";
    }
}
