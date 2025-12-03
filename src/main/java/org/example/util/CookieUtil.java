package org.example.util;

import jakarta.servlet.http.Cookie;
import org.example.model.User;
import org.example.model.UserSession;
import org.example.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CookieUtil {
    private final static boolean HTTP_ONLY = false;
    private final static String PATH = "/";
    private final static int MAX_AGE = 60 * 60; // 1 hour

    public static Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(PATH);
        cookie.setHttpOnly(HTTP_ONLY);
        cookie.setMaxAge(MAX_AGE); // 1 hour
        return cookie;
    }

    public static Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath(PATH);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(HTTP_ONLY);
        return cookie;
    }


}
