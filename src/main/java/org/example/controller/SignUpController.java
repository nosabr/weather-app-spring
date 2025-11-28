package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignUpController {
    @GetMapping("/sign-up")
    public String getSignUpPage(){
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String postSignUpPage(){
        return "sign-up";
    }
}
