package dev.gyuray.forum.controller;

import dev.gyuray.forum.domain.User;
import dev.gyuray.forum.repository.user.LoginForm;
import dev.gyuray.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm(
            @ModelAttribute LoginForm loginForm,
            Model model
    ) {
        model.addAttribute("hideLoginButtons", "true");
        return "users/loginForm";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute LoginForm loginForm,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request
    ) {
        User loginUser = userService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (loginUser == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            model.addAttribute("hideLoginButtons", "true");
            return "users/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
