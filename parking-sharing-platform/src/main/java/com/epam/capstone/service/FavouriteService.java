package com.epam.capstone.service;

import com.epam.capstone.model.Favourite;

import java.util.List;

public interface FavouriteService {

    Favourite add(Favourite favourite);

    boolean remove(Long favouriteId);

    Favourite findById(Long id);

    List<Favourite> findAll(int page, int size);

    List<Favourite> findByUserId(Long userId, int page, int size);

}
