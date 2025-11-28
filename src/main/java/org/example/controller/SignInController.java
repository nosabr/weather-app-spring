package org.example.controller;

import jakarta.servlet.http.HttpSession;
import org.example.dto.AuthResultDTO;
import org.example.model.User;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class SignInController {
    AuthService authService;

    @Autowired
    public SignInController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/sign-in")
    public String getSignInPage(@RequestParam(required = false) String error, Model model) {
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String processLogin(@RequestParam("login") String login,
                         @RequestParam("password") String password,
                         HttpSession session,
                         Model model) {
        AuthResultDTO authResultDTO = authService.authenticate(login, password);
        if(authResultDTO.getUser() != null) {

            return "redirect:/";
        }
        model.addAttribute("error", authResultDTO.getAuthError().getMessage());
        return "sign-in";
    }

}
