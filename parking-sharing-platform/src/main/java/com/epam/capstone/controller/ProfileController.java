package com.epam.capstone.controller;

import com.epam.capstone.dto.ChangePasswordDto;
import com.epam.capstone.dto.UserProfileDto;
import com.epam.capstone.exception.ProfileValidationException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.User;
import com.epam.capstone.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        model.addAttribute("user", getCurrentUser(session));

        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfilePage(HttpSession session, Model model) {

        User user = getCurrentUser(session);

        UserProfileDto dto = new UserProfileDto();

        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        model.addAttribute("profileDto", dto);

        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute("profileDto") UserProfileDto profileDto, HttpSession session, Model model) {

        User currentUser = (User) session.getAttribute("user");

        try {
            User updatedUser = userService.update(profileDto, getCurrentUser(session).getUserId());

            session.setAttribute("user", updatedUser);

            return "redirect:/profile";

        } catch (ProfileValidationException e) {

            model.addAttribute("profileDto", e.getProfileDto());

            return "edit-profile";
        }
    }

    @GetMapping("/profile/change-password")
    public String changePasswordPage(Model model) {

        model.addAttribute("changePasswordDto", new ChangePasswordDto());

        return "change-password";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(
            @ModelAttribute("changePasswordDto")
            ChangePasswordDto dto,
            HttpSession session,
            Model model) {

        User currentUser = getCurrentUser(session);

        dto.setCurrentPasswordError(null);
        dto.setNewPasswordError(null);
        dto.setConfirmPasswordError(null);
        dto.setCommonError(null);

        boolean hasErrors = false;

        if (dto.getCurrentPassword() == null
                || dto.getCurrentPassword().isBlank()) {

            dto.setCurrentPasswordError("Current password is required");
            hasErrors = true;
        }

        if (dto.getNewPassword() == null
                || dto.getNewPassword().isBlank()) {

            dto.setNewPasswordError("New password is required");
            hasErrors = true;
        }

        if (dto.getConfirmPassword() == null
                || dto.getConfirmPassword().isBlank()) {

            dto.setConfirmPasswordError("Please confirm password");
            hasErrors = true;
        }

        if (!hasErrors &&
                !dto.getNewPassword().equals(dto.getConfirmPassword())) {

            dto.setConfirmPasswordError("Passwords do not match");
            hasErrors = true;
        }

        if (hasErrors) {

            model.addAttribute("changePasswordDto", dto);

            return "change-password";
        }

        try {
            userService.changePassword(currentUser.getUserId(), dto.getCurrentPassword(), dto.getNewPassword());

            return "redirect:/profile";

        } catch (ValidationException e) {

            dto.setCommonError(e.getMessage());

            model.addAttribute("changePasswordDto", dto);

            return "change-password";
        }
    }
}