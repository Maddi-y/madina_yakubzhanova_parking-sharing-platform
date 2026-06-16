package com.epam.capstone.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {

    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll(int page, int size);

    T update(T entity);

    boolean deleteById(ID id);
}
