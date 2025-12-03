package org.example.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("userLogin")
    public String addUserLoginToModel(@CookieValue(value = "login", required = false) String login) {
        return login;
    }
}