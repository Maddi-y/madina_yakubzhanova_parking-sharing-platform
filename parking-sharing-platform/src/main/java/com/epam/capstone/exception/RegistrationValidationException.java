package com.epam.capstone.exception;

import com.epam.capstone.dto.UserRegistrationDto;

public class RegistrationValidationException extends RuntimeException {

    private final UserRegistrationDto registrationDto;

    public RegistrationValidationException(UserRegistrationDto registrationDto) {
        this.registrationDto = registrationDto;
    }

    public UserRegistrationDto getRegistrationDto() {
        return registrationDto;
    }
}
