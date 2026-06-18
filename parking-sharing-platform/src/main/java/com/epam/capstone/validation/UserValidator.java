package com.epam.capstone.validation;

import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.User;

import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9]{10,15}$");

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-zА-Яа-яЁёӘәҒғҚқҢңӨөҰұҮүҺһ\\s-]+$");

    @Override
    public void validate(User user) {

        if (user == null) {
            throw new ValidationException("User must not be null");
        }

        validateName(user.getName());
        validateEmail(user.getEmail());
        validatePhone(user.getPhone());
        validateRole(user);
        validateStatus(user);
    }

    private void validateName(String name) {

        if (name == null || name.isBlank()) {
            throw new ValidationException("Name is required");
        }

        if (name.length() < 2 || name.length() > 100) {
            throw new ValidationException("Name must contain from 2 to 100 characters");
        }

        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new ValidationException("Name contains invalid characters");
        }
    }

    private void validateEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new ValidationException("Email is required");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }
    }

    private void validatePhone(String phone) {

        if (phone == null || phone.isBlank()) {
            throw new ValidationException("Phone is required");
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("Invalid phone format");
        }
    }

    public void validatePassword(String password) {

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

    private void validateRole(User user) {

        if (user.getRole() == null) {
            throw new ValidationException("Role is required");
        }
    }

    private void validateStatus(User user) {

        if (user.getStatus() == null) {
            throw new ValidationException("Status is required");
        }
    }
}
