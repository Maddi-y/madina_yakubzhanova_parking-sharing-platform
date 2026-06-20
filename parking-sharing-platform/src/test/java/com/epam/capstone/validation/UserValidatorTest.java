package com.epam.capstone.validation;

import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.UserRole;
import com.epam.capstone.model.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private UserValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserValidator();
    }

    private User validUser() {
        return new User(
                null,
                UserRole.DRIVER,
                "John Doe",
                "john.doe@mail.com",
                "+77005894587",
                "hashed_password",
                UserStatus.ACTIVE,
                LocalDateTime.now()
        );
    }

    @Test
    void validate_ShouldPass_WhenUserIsValid() {
        User user = validUser();

        assertDoesNotThrow(() -> validator.validate(user));
    }

    @Test
    void validate_ShouldThrow_WhenUserIsNull() {
        assertThrows(ValidationException.class,
                () -> validator.validate(null));
    }

    @Test
    void validate_ShouldThrow_WhenNameIsNull() {
        User user = validUser();
        user.setName(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(user));
    }

    @Test
    void validate_ShouldThrow_WhenNameIsBlank() {
        User user = validUser();
        user.setName(" ");

        assertThrows(ValidationException.class,
                () -> validator.validate(user));
    }

    @Test
    void validate_ShouldThrow_WhenNameIsTooShort() {
        User user = validUser();
        user.setName("A");

        assertThrows(ValidationException.class,
                () -> validator.validate(user));
    }

    @Test
    void validate_ShouldThrow_WhenNameHasInvalidCharacters() {
        User user = validUser();
        user.setName("John123");

        assertThrows(ValidationException.class,
                () -> validator.validate(user));
    }

    @Test
    void validate_ShouldThrow_WhenEmailIsNull() {
        User user = validUser();
        user.setEmail(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(user));
    }

    @Test
    void validate_ShouldThrow_WhenEmailIsInvalid() {
        User user = validUser();
        user.setEmail("invalid-email");

        assertThrows(ValidationException.class,
                () -> validator.validate(user));
    }

    @Test
    void validate_ShouldThrow_WhenPhoneIsNull() {
        User user = validUser();
        user.setPhone(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(user));
    }

    @Test
    void validate_ShouldThrow_WhenPhoneIsInvalid() {
        User user = validUser();
        user.setPhone("123");

        assertThrows(ValidationException.class,
                () -> validator.validate(user));
    }

    @Test
    void validate_ShouldThrow_WhenRoleIsNull() {
        User user = validUser();
        user.setRole(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(user));
    }

    @Test
    void validate_ShouldThrow_WhenStatusIsNull() {
        User user = validUser();
        user.setStatus(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(user));
    }
}
