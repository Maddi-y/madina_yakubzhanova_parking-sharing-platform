package com.epam.capstone.validation;

import com.epam.capstone.dto.ResetPasswordDto;
import com.epam.capstone.exception.ResetPasswordValidationException;
import com.epam.capstone.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordValidator {

    private final PasswordValidator passwordValidator;

    public ResetPasswordValidator(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }

    public void validate(ResetPasswordDto dto) {

        validateRequiredFields(dto);
        validatePasswordsMatch(dto);
        validatePasswordStrength(dto);

        if (dto.hasErrors()) {
            throw new ResetPasswordValidationException(dto);
        }
    }

    private void validateRequiredFields(ResetPasswordDto dto) {

        if (dto.getNewPassword() == null || dto.getNewPassword().isBlank()) {
            dto.setNewPasswordError("New password is required");
        }

        if (dto.getConfirmPassword() == null || dto.getConfirmPassword().isBlank()) {
            dto.setConfirmPasswordError("Please confirm password");
        }
    }

    private void validatePasswordsMatch(ResetPasswordDto dto) {

        if (dto.getNewPassword() != null
                && dto.getConfirmPassword() != null
                && !dto.getNewPassword().equals(dto.getConfirmPassword())) {

            dto.setConfirmPasswordError("Passwords do not match");
        }
    }

    private void validatePasswordStrength(ResetPasswordDto dto) {

        if (dto.getNewPassword() == null || dto.getNewPassword().isBlank()) {
            return;
        }

        try {
            passwordValidator.validate(dto.getNewPassword());

        } catch (ValidationException e) {
            dto.setNewPasswordError(e.getMessage());
        }
    }
}