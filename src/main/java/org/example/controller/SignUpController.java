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
        RegistrationResultDTO registrationResultDTO = registrationService.register(login,password);
        if(registrationResultDTO.isSuccess()){
            redirectAttributes.addFlashAttribute("registrationSuccess", true);
            return "redirect:/sign-in";
        }
        model.addAttribute("error", registrationResultDTO.getRegistrationError().getMessage());
        return "sign-up";
    }
}
