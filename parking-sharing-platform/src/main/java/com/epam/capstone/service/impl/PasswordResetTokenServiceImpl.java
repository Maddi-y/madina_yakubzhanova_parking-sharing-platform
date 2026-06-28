package com.epam.capstone.service.impl;

import com.epam.capstone.dao.PasswordResetTokenDao;
import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.PasswordResetToken;
import com.epam.capstone.service.PasswordResetTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetTokenServiceImpl.class);

    private static final int TOKEN_LIFETIME_MINUTES = 30;

    private final PasswordResetTokenDao passwordResetTokenDao;

    public PasswordResetTokenServiceImpl(PasswordResetTokenDao passwordResetTokenDao) {
        this.passwordResetTokenDao = passwordResetTokenDao;
    }

    @Override
    public PasswordResetToken create(Long userId) {

        if (userId == null || userId <= 0) {
            throw new ValidationException("Invalid user id");
        }

        passwordResetTokenDao.deleteByUserId(userId);

        PasswordResetToken token = new PasswordResetToken();

        token.setUserId(userId);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(TOKEN_LIFETIME_MINUTES));
        token.setCreatedAt(LocalDateTime.now());

        PasswordResetToken savedToken = passwordResetTokenDao.save(token);

        LOGGER.info("Password reset token created. User ID={}", userId);

        return savedToken;
    }

    @Override
    public PasswordResetToken findByToken(String token) {

        if (token == null || token.isBlank()) {
            throw new ValidationException("Token is required");
        }

        PasswordResetToken resetToken = passwordResetTokenDao.findByToken(token).orElseThrow(() -> {

            LOGGER.warn("Password reset token not found");

            return new ServiceException("Invalid password reset token");});

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {

            LOGGER.warn("Password reset token expired");

            passwordResetTokenDao.deleteByToken(token);

            throw new ServiceException("Password reset token has expired");
        }

        return resetToken;
    }

    @Override
    public void deleteByToken(String token) {

        if (token == null || token.isBlank()) {
            throw new ValidationException("Token is required");
        }

        passwordResetTokenDao.deleteByToken(token);

        LOGGER.info("Password reset token deleted");
    }

    @Override
    public void deleteByUserId(Long userId) {

        if (userId == null || userId <= 0) {
            throw new ValidationException("Invalid user id");
        }

        passwordResetTokenDao.deleteByUserId(userId);

        LOGGER.info("Password reset tokens deleted. User ID={}", userId);
    }
}
