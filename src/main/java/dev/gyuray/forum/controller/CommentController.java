package dev.gyuray.forum.controller;

import dev.gyuray.forum.domain.User;
import dev.gyuray.forum.repository.comment.CommentForm;
import dev.gyuray.forum.repository.comment.CommentUpdateDTO;
import dev.gyuray.forum.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments/add")
    public String addComment(
            @PathVariable Long postId,
            @Validated @ModelAttribute CommentForm commentForm,
            BindingResult bindingResult,
            @SessionAttribute User loginUser
    ) {
        if (bindingResult.hasErrors()) {
            return "redirect:/posts/" + postId;
        }

        commentService.addComment(commentForm, postId, loginUser);
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
            @Validated @ModelAttribute CommentUpdateDTO commentUpdateDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "redirect:/posts/" + postId;
        }

        commentService.updateComment(commentId, commentUpdateDTO, loginUser);
        return "redirect:/posts/" + postId;
    }
}
