package com.epam.capstone.exception;

import com.epam.capstone.dto.ResetPasswordDto;

public class ResetPasswordValidationException extends ValidationException {

    private final ResetPasswordDto resetPasswordDto;

    public ResetPasswordValidationException(ResetPasswordDto dto) {

        super("Password reset validation failed");
        this.resetPasswordDto = dto;
    }

    public ResetPasswordDto getResetPasswordDto() {
        return resetPasswordDto;
    }
}
