package dev.gyuray.forum.controller;

import dev.gyuray.forum.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect("/users/login?redirectURL=" + requestURI);
            return false;
        } else {
            User loginUser = (User) session.getAttribute("loginUser");
            request.setAttribute("loginUser", loginUser);
            return true;
        }
    }
}
