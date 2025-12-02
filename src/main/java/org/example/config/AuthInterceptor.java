package org.example.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.UserSessionDao;
import org.example.model.User;
import org.example.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final UserSessionService userSessionService;

    @Autowired
    public AuthInterceptor(UserSessionService userSessionService, UserSessionDao userSessionDao) {
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String path = request.getServletPath();
        System.out.println("[AuthInterceptor] Checking: " + path);

        if (isUrlPublic(path)) {
            System.out.println("[AuthInterceptor] URL is public");
            return true;
        }
        User user = getCurrentUser(request);
        if (user == null) {
            System.out.println("[AuthInterceptor] User is null, redirect to login page");
            response.sendRedirect(request.getContextPath() + "/sign-in");
            return false;
        }

        System.out.println("[AuthInterceptor] Initialized as User: " + user.getLogin());
        request.setAttribute("currentUser", user);
        return true;
    }

    private User getCurrentUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            System.out.println("[AuthInterceptor] Cookies is null");
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("session_id")) {
                try {
                    UUID sessionId = UUID.fromString(cookie.getValue());
                    System.out.println("[AuthInterceptor] Cookie.getValue(): " + sessionId.toString());
                    User user = userSessionService.getUserBySessionId(sessionId);
                    if (user != null) {
                        System.out.println("[AuthInterceptor] User is already logged in");
                    } else {
                        System.out.println("[AuthInterceptor] User is not logged in");
                    }
                    return user;
                } catch (IllegalArgumentException e) {
                    System.out.println("[AuthInterceptor] Invalid session id");
                    return null;
                }
            }
        }
        return null;
    }

    private boolean isUrlPublic(String path) {
        return path.equals("/sign-in") || path.equals("/test")
                || path.equals("/sign-up") || path.equals("/error");
    }
}
