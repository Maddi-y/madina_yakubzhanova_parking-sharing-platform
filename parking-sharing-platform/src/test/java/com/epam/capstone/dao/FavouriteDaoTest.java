package com.epam.capstone.dao;

import com.epam.capstone.dao.impl.FavouriteDaoImpl;
import com.epam.capstone.dao.impl.ParkingSpotDaoImpl;
import com.epam.capstone.dao.impl.UserDaoImpl;
import com.epam.capstone.model.Favourite;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FavouriteDaoTest extends BaseDaoTest {

    private UserDao userDao;
    private ParkingSpotDao parkingSpotDao;
    private FavouriteDao favouriteDao;

    private TestData data;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(pool);
        parkingSpotDao = new ParkingSpotDaoImpl(pool);
        favouriteDao = new FavouriteDaoImpl(pool);
        data = null;
    }

    @AfterEach
    void tearDown() {
        if (data == null) return;

        try {
            if (data.favourite != null) {
                favouriteDao.deleteById(data.favourite.getFavouriteId());
            }
        } catch (Exception ignored) {}

        try {
            if (data.spot != null) {
                parkingSpotDao.deleteById(data.spot.getSpotId());
            }
        } catch (Exception ignored) {}

        try {
            if (data.owner != null) {
                userDao.deleteById(data.owner.getUserId());
            }
        } catch (Exception ignored) {}
    }

    private static class TestData {
        User owner;
        ParkingSpot spot;
        Favourite favourite;
    }

    private TestData createScenario() {
        TestData data = new TestData();

        data.owner = userDao.save(new User(
                null,
                UserRole.OWNER,
                "Favourite Owner",
                "fav_" + UUID.randomUUID() + "@mail.com",
                "+77082554460",
                "hash123",
                UserStatus.ACTIVE,
                LocalDateTime.now()
        ));

        data.spot = parkingSpotDao.save(new ParkingSpot(
                null,
                data.owner,
                "Favourite Spot",
                "Almaty",
                "Test parking spot",
                new BigDecimal("1500"),
                ParkingSpotStatus.AVAILABLE,
                new BigDecimal("43.238949"),
                new BigDecimal("76.889709"),
                null
        ));

        data.favourite = favouriteDao.save(new Favourite(null, data.owner, data.spot, null));

        return data;
    }

    @Test
    void save_ShouldCreateFavourite() {
        data = createScenario();

        assertNotNull(data.favourite.getFavouriteId());
    }

    @Test
    void findById_ShouldReturnFavourite() {
        data = createScenario();

        Optional<Favourite> result = favouriteDao.findById(data.favourite.getFavouriteId());

        assertTrue(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnFavourites() {
        data = createScenario();

        List<Favourite> result = favouriteDao.findAll(1, 10);

        assertFalse(result.isEmpty());
    }

    @Test
    void findByUserId_ShouldReturnFavourites() {
        data = createScenario();

        List<Favourite> result = favouriteDao.findByUserId(data.owner.getUserId(), 1, 10);

        assertFalse(result.isEmpty());
    }

    @Test
    void exists_ShouldReturnTrue_WhenFavouriteExists() {
        data = createScenario();

        boolean exists = favouriteDao.exists(data.owner.getUserId(), data.spot.getSpotId());

        assertTrue(exists);
    }

    @Test
    void update_ShouldReturnSameFavourite() {
        data = createScenario();

        Favourite updated = favouriteDao.update(data.favourite);

        assertNotNull(updated);
    }

    @Test
    void deleteById_ShouldRemoveFavourite() {
        data = createScenario();

        boolean deleted = favouriteDao.deleteById(data.favourite.getFavouriteId());

        assertTrue(deleted);
        assertTrue(favouriteDao.findById(data.favourite.getFavouriteId()).isEmpty());

        data.favourite = null;
    }
}