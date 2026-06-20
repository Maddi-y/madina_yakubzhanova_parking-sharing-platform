package com.epam.capstone.daoIntegrationTests;

import com.epam.capstone.dao.UserDao;
import com.epam.capstone.dao.impl.UserDaoImpl;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.UserRole;
import com.epam.capstone.model.enums.UserStatus;
import com.epam.capstone.pool.ConnectionPool;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserDaoIntegrationTest {
    public static void main(String[] args) {

        ConnectionPool pool = ConnectionPool.getInstance();

        UserDao userDao = new UserDaoImpl(pool);

        String email = "test.user@mail.com";

        User user = new User(
                null,
                UserRole.DRIVER,
                "Test User",
                email,
                "+77001234567",
                "hashed_password",
                UserStatus.ACTIVE,
                LocalDateTime.now()
        );

        System.out.println("=== SAVE ===");

        User savedUser = userDao.save(user);

        System.out.println(savedUser);

        Long userId = savedUser.getUserId();

        System.out.println("\n=== FIND BY ID ===");

        Optional<User> foundUser = userDao.findById(userId);

        System.out.println(foundUser);

        System.out.println("\n=== FIND BY EMAIL ===");

        Optional<User> userByEmail = userDao.findByEmail(email);

        System.out.println(userByEmail);

        System.out.println("\n=== EXISTS BY EMAIL ===");

        boolean exists = userDao.existsByEmail(email);

        System.out.println(exists);

        System.out.println("\n=== UPDATE ===");

        savedUser.setName("Updated User");

        User updatedUser = userDao.update(savedUser);

        System.out.println(updatedUser);

        System.out.println("\n=== FIND ALL ===");

        List<User> users = userDao.findAll(1, 10);

        users.forEach(System.out::println);

        System.out.println("\n=== DELETE ===");

        boolean deleted = userDao.deleteById(userId);

        System.out.println(deleted);

        System.out.println("\n=== CHECK DELETED ===");

        System.out.println(userDao.findById(userId));

        pool.shutdown();
    }
}
