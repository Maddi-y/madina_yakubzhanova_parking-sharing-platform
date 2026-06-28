package com.epam.capstone.service.impl;

import com.epam.capstone.dto.ResetPasswordDto;
import com.epam.capstone.exception.ResetPasswordValidationException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.PasswordResetToken;
import com.epam.capstone.model.User;
import com.epam.capstone.service.PasswordResetService;
import com.epam.capstone.service.PasswordResetTokenService;
import com.epam.capstone.service.UserService;
import com.epam.capstone.validation.ResetPasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetServiceImpl.class);

    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final ResetPasswordValidator resetPasswordValidator;

    public PasswordResetServiceImpl(UserService userService, PasswordResetTokenService passwordResetTokenService,
                                    ResetPasswordValidator resetPasswordValidator) {

        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.resetPasswordValidator = resetPasswordValidator;
    }

    @Override
    public String requestReset(String email) {

        if (email == null || email.isBlank()) {
            throw new ValidationException("Email is required");
        }

        User user = userService.findByEmail(email);

        PasswordResetToken token =
                passwordResetTokenService.create(user.getUserId());

        LOGGER.info(
                "Password reset requested. User={}, Token={}",
                user.getEmail(),
                token.getToken());

        return token.getToken();
    }

    @Override
    public void resetPassword(ResetPasswordDto dto) {

        resetPasswordValidator.validate(dto);

        PasswordResetToken resetToken = passwordResetTokenService.findByToken(dto.getToken());

        try {
            userService.resetPassword(resetToken.getUserId(), dto.getNewPassword());

        } catch (ValidationException e) {

            dto.setNewPasswordError(e.getMessage());

            throw new ResetPasswordValidationException(dto);
        }

        passwordResetTokenService.deleteByToken(dto.getToken());

        LOGGER.info("Password successfully reset. User ID={}", resetToken.getUserId());
    }
}
