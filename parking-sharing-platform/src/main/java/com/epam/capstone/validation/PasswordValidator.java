package com.epam.capstone.validation;

import com.epam.capstone.exception.ValidationException;
import org.springframework.stereotype.Component;

import com.epam.capstone.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {

    public void validate(String password) {

        if (password == null || password.isBlank()) {
            throw new ValidationException("Password is required");
        }

        if (password.length() < 8) {
            throw new ValidationException("Password must contain at least 8 characters");
        }

        if (!password.matches(".*\\d.*")) {
            throw new ValidationException("Password must contain at least one digit");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new ValidationException("Password must contain at least one uppercase letter");
        }
    }
}