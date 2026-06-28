package com.epam.capstone.dao;

import com.epam.capstone.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenDao {

    PasswordResetToken save(PasswordResetToken token);

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUserId(Long userId);
}
