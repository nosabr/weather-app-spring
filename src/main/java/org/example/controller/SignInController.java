package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.dao.UserSessionDao;
import org.example.dto.AuthResultDTO;
import org.example.model.User;
import org.example.model.UserSession;
import org.example.service.AuthService;
import org.example.service.UserSessionService;
import org.example.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SignInController {
    AuthService authService;
    UserSessionService userSessionService;

    @Autowired
    public SignInController(AuthService authService,  UserSessionService userSessionService) {
        this.authService = authService;
        this.userSessionService = userSessionService;
    }

    @GetMapping("/sign-in")
    public String getSignInPage(@RequestParam(required = false) String error, Model model) {
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String processLogin(@RequestParam("login") String login,
                         @RequestParam("password") String password,
                         HttpServletResponse response,
                         Model model) {
        AuthResultDTO authResultDTO = authService.authenticate(login, password);
        if(authResultDTO.isSuccess()) {
            UserSession userSession = userSessionService.createUserSession(authResultDTO.getUser());
            response.addCookie(CookieUtil.createCookie("login",userSession.getUser().getLogin()));
            response.addCookie(CookieUtil.createCookie("session_id", userSession.getId().toString()));
            return "redirect:/";
        }
        model.addAttribute("error", authResultDTO.getAuthError().getMessage());
        return "sign-in";
    }

}
