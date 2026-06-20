package com.epam.capstone.dao;

import com.epam.capstone.dao.impl.ParkingSpotDaoImpl;
import com.epam.capstone.dao.impl.UserDaoImpl;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotDaoTest extends BaseDaoTest {

    private UserDao userDao;
    private ParkingSpotDao parkingSpotDao;

    private TestData data;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(pool);
        parkingSpotDao = new ParkingSpotDaoImpl(pool);
        data = null;
    }

    @AfterEach
    void tearDown() {
        if (data == null) return;

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
    }

    private User createOwner() {
        return new User(
                null,
                UserRole.OWNER,
                "Parking Owner",
                "owner_" + UUID.randomUUID() + "@test.com",
                "+770698522233",
                "hashedPassword",
                UserStatus.ACTIVE,
                LocalDateTime.now()
        );
    }

    private ParkingSpot createSpot(User owner) {
        return new ParkingSpot(
                null,
                owner,
                "Test Parking",
                "Almaty, Abay 10",
                "Covered parking",
                new BigDecimal("500"),
                ParkingSpotStatus.AVAILABLE,
                new BigDecimal("43.238949"),
                new BigDecimal("76.889709"),
                null
        );
    }

    private TestData createScenario() {
        TestData data = new TestData();

        data.owner = userDao.save(createOwner());
        data.spot = parkingSpotDao.save(createSpot(data.owner));

        return data;
    }

    @Test
    void save_ShouldCreateParkingSpot() {
        data = createScenario();

        assertNotNull(data.spot.getSpotId());
        assertEquals("Test Parking", data.spot.getTitle());
        assertEquals(ParkingSpotStatus.AVAILABLE, data.spot.getStatus());
    }

    @Test
    void findById_ShouldReturnSpot_WhenExists() {
        data = createScenario();

        var result = parkingSpotDao.findById(data.spot.getSpotId());

        assertTrue(result.isPresent());
        assertEquals(data.spot.getSpotId(), result.get().getSpotId());
    }

    @Test
    void findAll_ShouldReturnSpots() {
        data = createScenario();

        var spots = parkingSpotDao.findAll(1, 10);

        assertFalse(spots.isEmpty());
    }

    @Test
    void findByOwnerId_ShouldReturnSpots() {
        data = createScenario();

        var spots = parkingSpotDao.findByOwnerId(data.owner.getUserId(), 1, 10);

        assertFalse(spots.isEmpty());
    }

    @Test
    void findAvailable_ShouldReturnAvailableSpots() {
        data = createScenario();

        var spots = parkingSpotDao.findAvailable(1, 10);

        assertNotNull(spots);
    }

    @Test
    void update_ShouldChangeTitle() {
        data = createScenario();

        data.spot.setTitle("Updated Parking");

        var updated = parkingSpotDao.update(data.spot);

        assertEquals("Updated Parking", updated.getTitle());
    }

    @Test
    void deleteById_ShouldRemoveSpot() {
        data = createScenario();

        boolean deleted = parkingSpotDao.deleteById(data.spot.getSpotId());

        assertTrue(deleted);
        assertTrue(parkingSpotDao.findById(data.spot.getSpotId()).isEmpty());

        data.spot = null;
    }
}