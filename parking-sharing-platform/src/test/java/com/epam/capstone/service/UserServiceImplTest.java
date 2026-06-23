package com.epam.capstone.service;


import com.epam.capstone.dao.UserDao;
import com.epam.capstone.dto.UserRegistrationDto;
import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.UserRole;
import com.epam.capstone.model.enums.UserStatus;
import com.epam.capstone.security.PasswordEncoder;
import com.epam.capstone.service.impl.UserServiceImpl;
import com.epam.capstone.validation.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private UserValidator userValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User buildUser() {

        User user = new User();

        user.setUserId(1L);
        user.setName("Test User");
        user.setEmail("test@mail.com");
        user.setPhone("+77001234567");
        user.setPasswordHash("Password123");
        user.setRole(UserRole.DRIVER);
        user.setStatus(UserStatus.ACTIVE);

        return user;
    }

    @Test
    void register_ShouldSaveUser_WhenDataIsValid() {

        User user = buildUser();

        when(userDao.existsByEmail(user.getEmail())).thenReturn(false);

        when(passwordEncoder.encode("Password123")).thenReturn("encoded-password");

        when(userDao.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.register(user);

        assertNotNull(result);

        assertEquals("encoded-password", result.getPasswordHash());

        verify(userValidator).validate(user);

        verify(userValidator).validatePassword("Password123");

        verify(userDao).save(user);
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {

        User user = buildUser();

        when(userDao.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(ValidationException.class, () -> userService.register(user));

        verify(userDao, never()).save(any());
    }

    @Test
    void register_ShouldThrowException_WhenUserIsNull() {

        assertThrows(ValidationException.class, () ->
                userService.register((UserRegistrationDto) null));

        verifyNoInteractions(userDao);
    }

    @Test
    void registerUser_ShouldThrowException_WhenUserIsNull() {

        assertThrows(ValidationException.class, () ->
                userService.register((User) null)
        );

        verifyNoInteractions(userDao);
    }

    @Test
    void register_ShouldEncodePassword() {

        User user = buildUser();

        when(userDao.existsByEmail(anyString())).thenReturn(false);

        when(passwordEncoder.encode("Password123")).thenReturn("encoded-password");

        when(userDao.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.register(user);

        verify(passwordEncoder).encode("Password123");
    }

    @Test
    void authenticate_ShouldReturnUser_WhenCredentialsAreValid() {

        User user = buildUser();

        user.setPasswordHash("encoded-password");

        when(userDao.findByEmail("test@mail.com")).thenReturn(java.util.Optional.of(user));

        when(passwordEncoder.matches("Password123", "encoded-password")).thenReturn(true);

        User result = userService.authenticate("test@mail.com", "Password123");

        assertNotNull(result);

        assertEquals(user.getUserId(), result.getUserId());

        verify(userDao).findByEmail("test@mail.com");

        verify(passwordEncoder).matches("Password123", "encoded-password");
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserNotFound() {

        when(userDao.findByEmail("test@mail.com")).thenReturn(java.util.Optional.empty());

        assertThrows(ValidationException.class, () -> userService.authenticate(
                        "test@mail.com",
                        "Password123"
                )
        );

        verify(userDao).findByEmail("test@mail.com");

        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void authenticate_ShouldThrowException_WhenPasswordIsInvalid() {

        User user = buildUser();

        user.setPasswordHash("encoded-password");

        when(userDao.findByEmail("test@mail.com")).thenReturn(java.util.Optional.of(user));

        when(passwordEncoder.matches("Password123", "encoded-password")).thenReturn(false);

        assertThrows(ValidationException.class, () -> userService.authenticate(
                        "test@mail.com",
                        "Password123"
                )
        );
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserIsBlocked() {

        User user = buildUser();

        user.setStatus(UserStatus.BLOCKED);
        user.setPasswordHash("encoded-password");

        when(userDao.findByEmail("test@mail.com")).thenReturn(java.util.Optional.of(user));

        when(passwordEncoder.matches("Password123", "encoded-password")).thenReturn(true);

        assertThrows(ValidationException.class, () -> userService.authenticate(
                        "test@mail.com",
                        "Password123"
                )
        );
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserIsDeleted() {

        User user = buildUser();

        user.setStatus(UserStatus.DELETED);
        user.setPasswordHash("encoded-password");

        when(userDao.findByEmail("test@mail.com")).thenReturn(java.util.Optional.of(user));

        when(passwordEncoder.matches("Password123", "encoded-password")).thenReturn(true);

        assertThrows(ValidationException.class, () -> userService.authenticate(
                        "test@mail.com",
                        "Password123"
                )
        );
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {

        User user = buildUser();

        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertNotNull(result);

        assertEquals(1L, result.getUserId());

        verify(userDao).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenUserNotFound() {

        when(userDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> userService.findById(1L));

        verify(userDao).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenIdIsInvalid() {

        assertThrows(ValidationException.class, () -> userService.findById(0L));

        verifyNoInteractions(userDao);
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenUserExists() {

        User user = buildUser();

        when(userDao.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("test@mail.com");

        assertNotNull(result);

        assertEquals("test@mail.com", result.getEmail());

        verify(userDao).findByEmail("test@mail.com");
    }

    @Test
    void findByEmail_ShouldThrowException_WhenUserNotFound() {

        when(userDao.findByEmail("test@mail.com")).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> userService.findByEmail("test@mail.com"));
    }

    @Test
    void findByEmail_ShouldThrowException_WhenEmailIsBlank() {

        assertThrows(ValidationException.class, () -> userService.findByEmail(" "));

        verifyNoInteractions(userDao);
    }

    @Test
    void findAll_ShouldReturnUsers() {

        User user1 = buildUser();

        User user2 = buildUser();
        user2.setUserId(2L);
        user2.setEmail("user2@mail.com");

        List<User> users = List.of(user1, user2);

        when(userDao.findAll(1, 10)).thenReturn(users);

        List<User> result = userService.findAll(1, 10);

        assertEquals(2, result.size());

        verify(userDao).findAll(1, 10);
    }

    @Test
    void findAll_ShouldThrowException_WhenPageIsInvalid() {

        assertThrows(ValidationException.class, () -> userService.findAll(0, 10));
        verifyNoInteractions(userDao);
    }

    @Test
    void findAll_ShouldThrowException_WhenSizeIsInvalid() {

        assertThrows(ValidationException.class, () -> userService.findAll(1, 0));
        verifyNoInteractions(userDao);
    }

    //успешное обновление
    @Test
    void update_ShouldUpdateUser_WhenDataIsValid() {

        User user = buildUser();

        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        when(userDao.update(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.update(user);

        assertNotNull(result);

        verify(userValidator).validate(user);

        verify(userDao).update(user);
    }

    //пользователь null
    @Test
    void update_ShouldThrowException_WhenUserIsNull() {

        assertThrows(ValidationException.class, () -> userService.update(null));
        verifyNoInteractions(userDao);
    }

    //userId отсутствует
    @Test
    void update_ShouldThrowException_WhenUserIdIsNull() {

        User user = buildUser();

        user.setUserId(null);

        assertThrows(ValidationException.class, () -> userService.update(user));
    }

    //пользователь удален
    @Test
    void update_ShouldThrowException_WhenUserIsDeleted() {

        User deletedUser = buildUser();

        deletedUser.setStatus(UserStatus.DELETED);

        when(userDao.findById(1L)).thenReturn(Optional.of(deletedUser));

        assertThrows(ValidationException.class, () -> userService.update(deletedUser));
    }

    //email уже занят другим пользователем
    @Test
    void update_ShouldThrowException_WhenEmailAlreadyExists() {

        User existingUser = buildUser();

        User userToUpdate = buildUser();

        userToUpdate.setEmail("new@mail.com");

        when(userDao.findById(1L)).thenReturn(Optional.of(existingUser));

        when(userDao.existsByEmail("new@mail.com")).thenReturn(true);

        assertThrows(ValidationException.class, () -> userService.update(userToUpdate));
    }

    //email не изменился
    @Test
    void update_ShouldNotCheckDuplicateEmail_WhenEmailNotChanged() {

        User user = buildUser();

        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        when(userDao.update(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.update(user);

        verify(userDao, never()).existsByEmail(anyString());
    }

    //успешное удаление
    @Test
    void deleteById_ShouldMarkUserAsDeleted() {

        User user = buildUser();

        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        when(userDao.update(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = userService.deleteById(1L);

        assertTrue(result);

        assertEquals(UserStatus.DELETED, user.getStatus());

        verify(userDao).update(user);
    }

    //пользователь уже удален
    @Test
    void deleteById_ShouldReturnFalse_WhenUserAlreadyDeleted() {

        User user = buildUser();

        user.setStatus(UserStatus.DELETED);

        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        boolean result = userService.deleteById(1L);

        assertFalse(result);

        verify(userDao, never()).update(any());
    }

    //некорректный id
    @Test
    void deleteById_ShouldThrowException_WhenIdIsInvalid() {

        assertThrows(ValidationException.class, () -> userService.deleteById(0L));
        verifyNoInteractions(userDao);
    }

    //успешная смена пароля
    @Test
    void changePassword_ShouldUpdatePassword() {

        User user = buildUser();

        user.setPasswordHash("encoded-old");

        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("OldPassword123", "encoded-old")).thenReturn(true);

        when(passwordEncoder.encode("NewPassword123")).thenReturn("encoded-new");

        when(userDao.update(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.changePassword(1L, "OldPassword123", "NewPassword123");

        assertEquals("encoded-new", result.getPasswordHash());

        verify(userValidator).validatePassword("NewPassword123");
    }

    //неверный старый пароль
    @Test
    void changePassword_ShouldThrowException_WhenOldPasswordIsInvalid() {

        User user = buildUser();

        user.setPasswordHash("encoded-old");

        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("WrongPassword", "encoded-old")).thenReturn(false);

        assertThrows(ValidationException.class, () -> userService.changePassword(
                        1L,
                        "WrongPassword",
                        "NewPassword123"
                )
        );
    }

    //новый пароль совпадает со старым
    @Test
    void changePassword_ShouldThrowException_WhenNewPasswordEqualsOldPassword() {

        User user = buildUser();

        user.setPasswordHash("encoded-old");

        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("Password123", "encoded-old")).thenReturn(true);

        assertThrows(ValidationException.class, () -> userService.changePassword(
                        1L,
                        "Password123",
                        "Password123"
                )
        );
    }

    //пользователь не ACTIVE
    @Test
    void changePassword_ShouldThrowException_WhenUserIsBlocked() {

        User user = buildUser();

        user.setStatus(UserStatus.BLOCKED);

        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(ValidationException.class, () -> userService.changePassword(
                        1L,
                        "OldPassword123",
                        "NewPassword123"
                )
        );
    }

    //некорректный userId
    @Test
    void changePassword_ShouldThrowException_WhenUserIdIsInvalid() {

        assertThrows(ValidationException.class, () -> userService.changePassword(
                        0L,
                        "OldPassword123",
                        "NewPassword123"
                )
        );

        verifyNoInteractions(userDao);
    }

}
