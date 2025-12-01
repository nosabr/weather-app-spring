package org.example.controller;

import org.example.dto.RegistrationResultDTO;
import org.example.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {

    RegistrationService registrationService;

    @Autowired
    public SignUpController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/sign-up")
    public String getSignUpPage(){
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String postSignUpPage(@RequestParam("login") String login,
                                 @RequestParam("password") String password,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model){
        if(!isValidLogin(login)){
            model.addAttribute("error", "Login is empty");
            return "sign-up";
        }
        if(!isValidPassword(password, confirmPassword)){
            model.addAttribute("error", "Invalid Password");
            return "sign-up";
        }

        RegistrationResultDTO registrationResultDTO = registrationService.register(login,password);

    }
    private boolean isValidLogin(String login){
        return login != null && !login.isEmpty() && login.matches("^[A-Za-z0-9]{6,}$");
    }
    private boolean isValidPassword(String password, String confirmPassword){
        return password != null && !password.isEmpty() &&  password.equals(confirmPassword);
    }
}
