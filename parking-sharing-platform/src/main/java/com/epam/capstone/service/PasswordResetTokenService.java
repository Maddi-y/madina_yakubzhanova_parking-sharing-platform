package com.epam.capstone.service;

import com.epam.capstone.model.PasswordResetToken;

public interface PasswordResetTokenService {

    PasswordResetToken create(Long userId);

    PasswordResetToken findByToken(String token);

    void deleteByToken(String token);

    void deleteByUserId(Long userId);
}
