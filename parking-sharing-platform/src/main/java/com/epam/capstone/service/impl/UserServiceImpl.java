package com.epam.capstone.service.impl;


import com.epam.capstone.dao.UserDao;
import com.epam.capstone.dto.UserRegistrationDto;
import com.epam.capstone.exception.RegistrationValidationException;
import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.UserRole;
import com.epam.capstone.model.enums.UserStatus;
import com.epam.capstone.security.PasswordEncoder;
import com.epam.capstone.service.UserService;
import com.epam.capstone.validation.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, UserValidator userValidator, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {

        if (user == null) {
            throw new ValidationException("User must not be null");
        }

        user.setEmail(
                user.getEmail().trim().toLowerCase()
        );

        userValidator.validate(user);
        userValidator.validatePassword(user.getPasswordHash());

        if (userDao.existsByEmail(user.getEmail())) {
            LOGGER.warn("Registration attempt with existing email: {}", user.getEmail());
            throw new ValidationException("User with email '%s' already exists".formatted(user.getEmail()));
        }

        if (userDao.existsByPhone(user.getPhone())) {
            LOGGER.warn("Registration attempt with existing phone: {}", user.getPhone());

            throw new ValidationException("User with phone '%s' already exists".formatted(user.getPhone()));
        }


        String encodedPassword = passwordEncoder.encode(user.getPasswordHash());

        user.setPasswordHash(encodedPassword);

        User savedUser = userDao.save(user);

        LOGGER.info("User registered successfully. ID={}, Email={}",
                savedUser.getUserId(),
                savedUser.getEmail()
        );

        return savedUser;
    }

    @Override
    public User register(UserRegistrationDto dto) {

        if (dto == null) {
            throw new ValidationException("Registration data must not be null");
        }

        dto.setNameError(null);
        dto.setEmailError(null);
        dto.setPhoneError(null);
        dto.setPasswordError(null);

        boolean hasErrors = false;

        try {
            userValidator.validateName(dto.getName());
        } catch (ValidationException e) {
            dto.setNameError(e.getMessage());
            hasErrors = true;
        }

        try {
            userValidator.validateEmail(dto.getEmail());
        } catch (ValidationException e) {
            dto.setEmailError(e.getMessage());
            hasErrors = true;
        }

        try {
            userValidator.validatePhone(dto.getPhone());
        } catch (ValidationException e) {
            dto.setPhoneError(e.getMessage());
            hasErrors = true;
        }

        try {
            userValidator.validatePassword(dto.getPassword());
        } catch (ValidationException e) {
            dto.setPasswordError(e.getMessage());
            hasErrors = true;
        }

        if (dto.getEmail() != null
                && !dto.getEmail().isBlank()
                && userDao.existsByEmail(dto.getEmail().trim().toLowerCase())) {

            LOGGER.warn("Registration attempt with existing email: {}", dto.getEmail());

            dto.setEmailError(
                    "User with email '%s' already exists".formatted(dto.getEmail()));

            hasErrors = true;
        }

        if (dto.getPhone() != null
                && !dto.getPhone().isBlank()
                && userDao.existsByPhone(dto.getPhone())) {

            LOGGER.warn("Registration attempt with existing phone: {}", dto.getPhone());

            dto.setPhoneError("User with phone '%s' already exists".formatted(dto.getPhone()));

            hasErrors = true;
        }

        if (hasErrors) {
            throw new RegistrationValidationException(dto);
        }

        User user = new User();

        user.setRole(UserRole.DRIVER);
        user.setName(dto.getName());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setPhone(dto.getPhone());
        user.setPasswordHash(dto.getPassword());
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());

        return register(user);
    }

    @Override
    public User authenticate(String email, String password) {

        if (email == null || email.isBlank()) {
            throw new ValidationException("Email is required");
        }

        if (password == null || password.isBlank()) {
            throw new ValidationException("Password is required");
        }

        String normalizedEmail = email.trim().toLowerCase();

        User user = userDao.findByEmail(normalizedEmail).orElseThrow(() -> {
            LOGGER.warn("Authentication failed. User not found: {}", normalizedEmail);

            return new ValidationException("Invalid email or password");
        });

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            LOGGER.warn(
                    "Authentication failed. Invalid password for email: {}", normalizedEmail);

            throw new ValidationException("Invalid email or password"
            );
        }

        if (user.getStatus() != UserStatus.ACTIVE) {

            LOGGER.warn("Authentication failed. User status={}. Email={}", user.getStatus(), normalizedEmail);

            throw new ValidationException("User account is not active");
        }

        LOGGER.info(
                "User authenticated successfully. ID={}, Email={}", user.getUserId(), user.getEmail());
        return user;
    }

    @Override
    public User findById(Long id) {

        if (id == null || id <= 0) {
            throw new ValidationException("Invalid user id");
        }

        return userDao.findById(id).orElseThrow(() -> {
            LOGGER.warn("User not found. ID={}", id);
            return new ServiceException("User not found");
        });
    }

    @Override
    public User findByEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new ValidationException("Email is required");
        }

        String normalizedEmail = email.trim().toLowerCase();

        return userDao.findByEmail(normalizedEmail)
                .orElseThrow(() -> {
                    LOGGER.warn("User not found. Email={}", normalizedEmail);

                    return new ServiceException("User not found");
                });
    }

    @Override
    public List<User> findAll(int page, int size) {

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }
        return userDao.findAll(page, size);
    }

    @Override
    public User update(User user) {

        if (user == null) {
            throw new ValidationException("User must not be null");
        }

        if (user.getUserId() == null) {
            throw new ValidationException("User id is required");
        }

        userValidator.validate(user);

        User existingUser = findById(user.getUserId());

        if (existingUser.getStatus() == UserStatus.DELETED) {

            LOGGER.warn(
                    "Attempt to update deleted user. ID={}",
                    user.getUserId()
            );

            throw new ValidationException(
                    "Deleted user cannot be updated"
            );
        }

        String normalizedEmail = user.getEmail().trim().toLowerCase();

        if (!existingUser.getEmail().equalsIgnoreCase(normalizedEmail) && userDao.existsByEmail(normalizedEmail)) {
            throw new ValidationException("Email already exists");
        }

        user.setEmail(normalizedEmail);

        User updatedUser = userDao.update(user);

        LOGGER.info(
                "User updated successfully. ID={}", updatedUser.getUserId());

        return updatedUser;
    }

    @Override
    public boolean deleteById(Long id) {

        if (id == null || id <= 0) {
            throw new ValidationException("Invalid user id");
        }

        User user = findById(id);

        if (user.getStatus() == UserStatus.DELETED) {
            LOGGER.warn("User already deleted. ID={}", id);
            return false;
        }

        user.setStatus(UserStatus.DELETED);

        userDao.update(user);

        LOGGER.info("User deleted successfully. ID={}", id);

        return true;
    }

    @Override
    public User changePassword(Long userId, String oldPassword, String newPassword) {

        if (userId == null || userId <= 0) {
            throw new ValidationException("Invalid user id");
        }

        if (oldPassword == null || oldPassword.isBlank()) {
            throw new ValidationException("Current password is required");
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new ValidationException("New password is required");
        }

        User user = findById(userId);

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ValidationException("User account is not active");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            LOGGER.warn("Password change failed. Invalid current password. User ID={}", userId);

            throw new ValidationException("Current password is incorrect");
        }

        if (passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new ValidationException("New password must be different from current password");
        }

        userValidator.validatePassword(newPassword);

        user.setPasswordHash(passwordEncoder.encode(newPassword));

        User updatedUser = userDao.update(user);

        LOGGER.info("Password changed successfully. User ID={}", userId);

        return updatedUser;
    }
}
