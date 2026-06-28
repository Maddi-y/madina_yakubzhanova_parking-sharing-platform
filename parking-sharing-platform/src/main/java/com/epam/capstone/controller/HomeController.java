package com.epam.capstone.controller;

import org.springframework.ui.Model;
import com.epam.capstone.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session,
                       Model model) {

        User user = (User) session.getAttribute("user");

        model.addAttribute("user", user);

        return "home";
    }
}
