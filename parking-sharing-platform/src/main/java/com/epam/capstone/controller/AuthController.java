package com.epam.capstone.controller;

import com.epam.capstone.dto.UserLoginDto;
import com.epam.capstone.dto.UserRegistrationDto;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.exception.RegistrationValidationException;
import com.epam.capstone.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {

        model.addAttribute("loginDto", new UserLoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginDto") UserLoginDto loginDto) {

        System.out.println("EMAIL = " + loginDto.getEmail());
        System.out.println("PASSWORD = " + loginDto.getPassword());

        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("registrationDto", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registrationDto") UserRegistrationDto registrationDto, Model model) {

        try {
            userService.register(registrationDto);

            return "redirect:/login";

        } catch (RegistrationValidationException e) {

            model.addAttribute("registrationDto", e.getRegistrationDto());

            return "register";
        }
    }


}
