package com.epam.capstone.dao;

import com.epam.capstone.model.User;

import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
