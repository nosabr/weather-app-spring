package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.model.UserSession;
import org.example.service.UserSessionService;
import org.example.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class LogoutController {
    UserSessionService userSessionService;
    @Autowired
    public LogoutController(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @GetMapping("/sign-out")
    public String signOut(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("session_id")) {
                userSessionService.deleteUserSession(cookie.getValue());
                System.out.println("[LogoutController] sessionId deleted]");
                break;
            }
        }
        response.addCookie(CookieUtil.deleteCookie("session_id"));
        response.addCookie(CookieUtil.deleteCookie("login"));
        return "redirect:/";
    }
}
