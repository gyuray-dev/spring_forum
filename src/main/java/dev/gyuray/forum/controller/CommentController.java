package dev.gyuray.forum.controller;

import dev.gyuray.forum.domain.User;
import dev.gyuray.forum.repository.comment.CommentForm;
import dev.gyuray.forum.repository.comment.CommentUpdateDTO;
import dev.gyuray.forum.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments/add")
    public String addComment(
            @PathVariable Long postId,
            @ModelAttribute CommentForm commentForm,
            @SessionAttribute User loginUser
    ) {
        commentService.addComment(commentForm, loginUser);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/posts/{postId}/comments/{commentId}/delete")
    public String deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @SessionAttribute User loginUser
    ) {
        commentService.deleteComment(commentId, loginUser);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/edit")
    public String editComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @SessionAttribute User loginUser,
            @ModelAttribute CommentUpdateDTO commentUpdateDTO
    ) {
        commentService.updateComment(commentId, commentUpdateDTO, loginUser);
        return "redirect:/posts/" + postId;
    }
}
