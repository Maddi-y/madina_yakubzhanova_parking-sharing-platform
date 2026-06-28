package com.epam.capstone.service;

import com.epam.capstone.dto.ResetPasswordDto;

public interface PasswordResetService {

    String requestReset(String email);

    void resetPassword(ResetPasswordDto dto);
}
