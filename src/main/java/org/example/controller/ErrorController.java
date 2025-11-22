package org.example.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        String errorCode = "Error";
        String errorMessage = "Something went wrong";
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == 404) {
                errorCode = "404";
                errorMessage = "The requested resource was not found";
            }
            else if (statusCode == 500) {
                errorCode = "500";
                errorMessage = "Internal Server Error";
            }
            else if (statusCode == 403) {
                errorCode = "403";
                errorMessage = "Access Forbidden";
            }
            else {
                errorCode = String.valueOf(statusCode);
            }
        }
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", errorMessage);

        return "error";
    }
}
