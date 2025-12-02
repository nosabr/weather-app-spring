package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.model.User;
import org.example.model.UserSession;
import org.example.service.UserSessionService;
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
    public String signOut(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("session_id")) {
                String sessionId = cookie.getValue();
                userSessionService.deleteUserSession(sessionId);
                System.out.println("[LogoutController] sessionId deleted]");
            }
        }
        return "redirect:/";
    }
}
