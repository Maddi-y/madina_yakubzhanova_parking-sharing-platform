package com.epam.capstone.dao;

import com.epam.capstone.dao.impl.UserDaoImpl;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.UserRole;
import com.epam.capstone.model.enums.UserStatus;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest extends BaseDaoTest {

    private UserDao userDao;
    private User createdUser;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(pool);
        createdUser = null;
    }

    @AfterEach
    void tearDown() {
        if (createdUser != null && createdUser.getUserId() != null) {
            userDao.deleteById(createdUser.getUserId());
        }
    }

    private User createUser() {
        return new User(
                null,
                UserRole.DRIVER,
                "Test User",
                "test_" + UUID.randomUUID() + "@mail.com",
                "+77001234567",
                "hashed_password",
                UserStatus.ACTIVE,
                LocalDateTime.now()
        );
    }

    @Test
    void save_ShouldCreateUser() {

        User user = createUser();

        createdUser = userDao.save(user);

        assertNotNull(createdUser.getUserId());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    void findById_ShouldReturnUser_WhenExists() {

        createdUser = userDao.save(createUser());

        var result = userDao.findById(createdUser.getUserId());

        assertTrue(result.isPresent());
    }

    @Test
    void findByEmail_ShouldReturnUser() {

        createdUser = userDao.save(createUser());

        var result = userDao.findByEmail(createdUser.getEmail());

        assertTrue(result.isPresent());
    }

    @Test
    void existsByEmail_ShouldReturnTrue() {

        createdUser = userDao.save(createUser());

        assertTrue(userDao.existsByEmail(createdUser.getEmail()));
    }

    @Test
    void update_ShouldChangeUserName() {

        createdUser = userDao.save(createUser());

        createdUser.setName("Updated User");

        User updated = userDao.update(createdUser);

        assertEquals("Updated User", updated.getName());
    }

    @Test
    void findAll_ShouldReturnUsers() {

        createdUser = userDao.save(createUser());

        var users = userDao.findAll(1, 10);

        assertFalse(users.isEmpty());
    }

    @Test
    void deleteById_ShouldRemoveUser() {

        createdUser = userDao.save(createUser());

        boolean deleted = userDao.deleteById(createdUser.getUserId());

        assertTrue(deleted);

        assertTrue(userDao.findById(createdUser.getUserId()).isEmpty());

        createdUser = null;
    }
}