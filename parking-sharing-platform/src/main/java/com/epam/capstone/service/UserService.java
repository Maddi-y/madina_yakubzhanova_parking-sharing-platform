package com.epam.capstone.service;

import com.epam.capstone.dto.UserRegistrationDto;
import com.epam.capstone.model.User;

import java.util.List;

public interface UserService {

    User register(UserRegistrationDto dto);

    User register(User user);

    User authenticate(String email, String password);

    User findById(Long id);

    User findByEmail(String email);

    List<User> findAll(int page, int size);

    User update(User user);

    boolean deleteById(Long id);

    User changePassword(Long userId, String oldPassword, String newPassword);
}
