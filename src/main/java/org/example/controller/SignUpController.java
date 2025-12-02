package org.example.controller;

import org.example.dao.UserDao;
import org.example.dto.RegistrationResultDTO;
import org.example.model.User;
import org.example.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                                 Model model,
                                 RedirectAttributes redirectAttributes){
        if(!isValidLogin(login)){
            model.addAttribute("error", "Login is empty");
            return "sign-up";
        }
        if(!isValidPassword(password, confirmPassword)){
            model.addAttribute("error", "Invalid Password");
            return "sign-up";
        }
        RegistrationResultDTO registrationResultDTO = registrationService.register(login,password);
        if(registrationResultDTO.getUser() == null){
            model.addAttribute("error", registrationResultDTO.getRegistrationError().getMessage());
            return "sign-up";
        }
        redirectAttributes.addFlashAttribute("registrationSuccess", true);
        return "redirect:/sign-in";
    }
    private boolean isValidLogin(String login){
        return login != null && !login.isEmpty();
    }
    private boolean isValidPassword(String password, String confirmPassword){
        return password != null && !password.isEmpty() &&  password.equals(confirmPassword);
    }
}
