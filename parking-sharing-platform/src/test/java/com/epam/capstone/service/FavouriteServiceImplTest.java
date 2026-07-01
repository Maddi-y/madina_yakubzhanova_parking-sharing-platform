package com.epam.capstone.service;

import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavouriteServiceImplTest {

    @Mock
    private FavouriteDao favouriteDao;

    @Mock
    private FavouriteValidator favouriteValidator;

    @InjectMocks
    private FavouriteServiceImpl favouriteService;

    private Favourite buildFavourite() {

        User user = new User();
        user.setUserId(1L);

        ParkingSpot spot = new ParkingSpot();
        spot.setSpotId(10L);

        Favourite favourite = new Favourite();
        favourite.setFavouriteId(100L);
        favourite.setUser(user);
        favourite.setParkingSpot(spot);
        favourite.setCreatedAt(LocalDateTime.now());

        return favourite;
    }

    @Test
    void add_ShouldSaveFavourite() {

        Favourite favourite = buildFavourite();

        when(favouriteDao.exists(1L, 10L)).thenReturn(false);
        when(favouriteDao.save(any(Favourite.class))).thenReturn(favourite);

        Favourite result = favouriteService.add(favourite);

        assertNotNull(result);

        assertEquals(favourite.getFavouriteId(), result.getFavouriteId());

        verify(favouriteValidator).validate(favourite);
        verify(favouriteDao).save(favourite);
    }

    @Test
    void add_ShouldThrowException_WhenFavouriteAlreadyExists() {

        Favourite favourite = buildFavourite();

        when(favouriteDao.exists(1L, 10L)).thenReturn(true);

        assertThrows(ValidationException.class, () -> favouriteService.add(favourite));

        verify(favouriteDao, never()).save(any());
    }

    @Test
    void add_ShouldThrowException_WhenFavouriteIsNull() {

        assertThrows(ValidationException.class, () -> favouriteService.add(null));
    }

    @Test
    void remove_ShouldDeleteFavourite() {

        Favourite favourite = buildFavourite();

        when(favouriteDao.findById(100L)).thenReturn(java.util.Optional.of(favourite));
        when(favouriteDao.deleteById(100L)).thenReturn(true);

        boolean result = favouriteService.remove(100L);

        assertTrue(result);

        verify(favouriteDao).deleteById(100L);
    }

    @Test
    void remove_ShouldThrowException_WhenIdInvalid() {

        assertThrows(ValidationException.class, () -> favouriteService.remove(0L));
    }

    @Test
    void findById_ShouldReturnFavourite() {

        Favourite favourite = buildFavourite();

        when(favouriteDao.findById(100L)).thenReturn(java.util.Optional.of(favourite));

        Favourite result = favouriteService.findById(100L);

        assertNotNull(result);
        assertEquals(favourite.getFavouriteId(), result.getFavouriteId());
    }

    @Test
    void findById_ShouldThrowException_WhenFavouriteNotFound() {

        when(favouriteDao.findById(100L)).thenReturn(java.util.Optional.empty());

        assertThrows(ServiceException.class, () -> favouriteService.findById(100L));
    }

    @Test
    void findById_ShouldThrowException_WhenIdInvalid() {

        assertThrows(ValidationException.class, () -> favouriteService.findById(-1L));
    }

    @Test
    void findAll_ShouldReturnFavourites() {

        List<Favourite> favourites = List.of(buildFavourite());

        when(favouriteDao.findAll(1, 10)).thenReturn(favourites);

        var result = favouriteService.findAll(1, 10);

        assertEquals(1, result.size());

        verify(favouriteDao).findAll(1, 10);
    }

    @Test
    void findAll_ShouldThrowException_WhenPageInvalid() {

        assertThrows(ValidationException.class, () -> favouriteService.findAll(0, 10));
    }

    @Test
    void findAll_ShouldThrowException_WhenSizeInvalid() {

        assertThrows(ValidationException.class, () -> favouriteService.findAll(1, 0));
    }

    @Test
    void findByUserId_ShouldReturnFavourites() {

        List<Favourite> favourites = List.of(buildFavourite());

        when(favouriteDao.findByUserId(1L, 1, 10)).thenReturn(favourites);

        var result = favouriteService.findByUserId(1L, 1, 10);

        assertEquals(1, result.size());

        verify(favouriteDao).findByUserId(1L, 1, 10);
    }

    @Test
    void findByUserId_ShouldThrowException_WhenUserIdInvalid() {

        assertThrows(ValidationException.class, () -> favouriteService.findByUserId(0L, 1, 10));
    }

    @Test
    void findByUserId_ShouldThrowException_WhenPageInvalid() {

        assertThrows(ValidationException.class, () -> favouriteService.findByUserId(1L, 0, 10));
    }

    @Test
    void findByUserId_ShouldThrowException_WhenSizeInvalid() {

        assertThrows(ValidationException.class, () -> favouriteService.findByUserId(1L, 1, 0));
    }
}