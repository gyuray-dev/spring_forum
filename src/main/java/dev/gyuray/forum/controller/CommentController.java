package dev.gyuray.forum.controller;

import dev.gyuray.forum.repository.comment.CommentForm;
import dev.gyuray.forum.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments/add")
    public String addComment(
            @PathVariable Long postId,
            @ModelAttribute CommentForm commentForm
    ) {
        commentService.addComment(commentForm);

        return "redirect:/posts/" + postId;
    }
}
