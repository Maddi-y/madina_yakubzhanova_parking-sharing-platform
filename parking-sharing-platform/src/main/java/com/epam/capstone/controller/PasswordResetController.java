package com.epam.capstone.controller;

import com.epam.capstone.dto.ForgotPasswordDto;
import com.epam.capstone.dto.ResetPasswordDto;
import com.epam.capstone.exception.ResetPasswordValidationException;
import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.PasswordResetToken;
import com.epam.capstone.service.PasswordResetService;
import com.epam.capstone.service.PasswordResetTokenService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final PasswordResetTokenService passwordResetTokenService;

    public PasswordResetController(PasswordResetService passwordResetService, PasswordResetTokenService passwordResetTokenService) {
        this.passwordResetService = passwordResetService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {

        model.addAttribute("forgotPasswordDto", new ForgotPasswordDto());

        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@ModelAttribute ForgotPasswordDto dto, Model model) {

        try {
            String token = passwordResetService.requestReset(dto.getEmail());

            model.addAttribute("resetLink", "/reset-password?token=" + token);

            return "forgot-password-success";

        } catch (ValidationException e) {

            dto.setEmailError(e.getMessage());

            model.addAttribute("forgotPasswordDto", dto);

            return "forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam("token") String token, Model model) {

        try {
            PasswordResetToken resetToken = passwordResetTokenService.findByToken(token);

            ResetPasswordDto dto = new ResetPasswordDto();
            dto.setToken(resetToken.getToken());

            model.addAttribute("resetPasswordDto", dto);

            return "reset-password";

        } catch (ServiceException e) {

            model.addAttribute("message", e.getMessage());

            return "invalid-reset-token";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@ModelAttribute("resetPasswordDto") ResetPasswordDto dto, Model model) {

        try {
            passwordResetService.resetPassword(dto);

            return "password-reset-success";

        } catch (ResetPasswordValidationException e) {

            model.addAttribute("resetPasswordDto", e.getResetPasswordDto());

            return "reset-password";
        }
    }
}
