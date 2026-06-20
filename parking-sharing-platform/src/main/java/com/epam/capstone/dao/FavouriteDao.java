package com.epam.capstone.dao;

import com.epam.capstone.model.Favourite;

import java.util.List;

public interface FavouriteDao extends GenericDao<Favourite, Long> {

    List<Favourite> findByUserId(Long userId, int page, int size);

    boolean exists(Long userId, Long spotId);
}
