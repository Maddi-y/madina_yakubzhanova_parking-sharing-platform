package com.epam.capstone.service;

import com.epam.capstone.dao.ParkingSpotDao;
import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.ParkingSpotStatus;
import com.epam.capstone.service.impl.ParkingSpotServiceImpl;
import com.epam.capstone.validation.ParkingSpotValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingSpotServiceImplTest {

    @Mock
    private ParkingSpotDao parkingSpotDao;

    @Mock
    private ParkingSpotValidator parkingSpotValidator;

    @InjectMocks
    private ParkingSpotServiceImpl parkingSpotService;

    private ParkingSpot buildParkingSpot() {

        User owner = new User();
        owner.setUserId(1L);

        ParkingSpot spot = new ParkingSpot();

        spot.setSpotId(1L);
        spot.setOwner(owner);
        spot.setTitle("Parking near Mega");
        spot.setAddress("Almaty, Abay 10");
        spot.setDescription("Covered parking spot");
        spot.setHourlyRate(BigDecimal.valueOf(1000));
        spot.setStatus(ParkingSpotStatus.AVAILABLE);
        spot.setLatitude(BigDecimal.valueOf(43.238949));
        spot.setLongitude(BigDecimal.valueOf(76.889709));

        return spot;
    }

    @Test
    void create_ShouldSaveParkingSpot_WhenDataIsValid() {

        ParkingSpot spot = buildParkingSpot();

        when(parkingSpotDao.save(any(ParkingSpot.class))).thenReturn(spot);

        ParkingSpot result = parkingSpotService.create(spot);

        assertNotNull(result);

        verify(parkingSpotValidator).validate(spot);
        verify(parkingSpotDao).save(spot);
    }

    @Test
    void create_ShouldThrowException_WhenParkingSpotIsNull() {

        assertThrows(ValidationException.class, () ->
                parkingSpotService.create(null));
        verifyNoInteractions(parkingSpotDao);
    }

    @Test
    void findById_ShouldReturnSpot_WhenExists() {

        ParkingSpot spot = buildParkingSpot();

        when(parkingSpotDao.findById(1L)).thenReturn(Optional.of(spot));

        ParkingSpot result = parkingSpotService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getSpotId());

        verify(parkingSpotDao).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenIdInvalid() {

        ValidationException exception = assertThrows(ValidationException.class, () ->
                parkingSpotService.findById(0L));

        assertEquals("Invalid parking spot id", exception.getMessage());

        verifyNoInteractions(parkingSpotDao);
    }

    @Test
    void findById_ShouldThrowException_WhenSpotNotFound() {

        when(parkingSpotDao.findById(1L)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () ->
                parkingSpotService.findById(1L));

        assertEquals("Parking spot not found", exception.getMessage());

        verify(parkingSpotDao).findById(1L);
    }

    @Test
    void findAll_ShouldReturnList_WhenArgumentsValid() {

        List<ParkingSpot> spots = List.of(buildParkingSpot(), buildParkingSpot());

        when(parkingSpotDao.findAll(1, 10)).thenReturn(spots);

        List<ParkingSpot> result = parkingSpotService.findAll(1, 10);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(parkingSpotDao).findAll(1, 10);
    }

    @Test
    void findAll_ShouldThrowException_WhenPageInvalid() {

        ValidationException exception = assertThrows(ValidationException.class, () ->
                parkingSpotService.findAll(0, 10)
        );

        assertEquals("Page must be greater than zero", exception.getMessage());
        verifyNoInteractions(parkingSpotDao);
    }

    @Test
    void findAll_ShouldThrowException_WhenSizeInvalid() {

        ValidationException exception = assertThrows(ValidationException.class, () ->
                parkingSpotService.findAll(1, 0)
        );

        assertEquals("Size must be greater than zero", exception.getMessage());
        verifyNoInteractions(parkingSpotDao);
    }

    @Test
    void update_ShouldUpdateSpot_WhenDataValid() {

        ParkingSpot spot = buildParkingSpot();

        when(parkingSpotDao.findById(1L)).thenReturn(Optional.of(spot));
        when(parkingSpotDao.update(spot)).thenReturn(spot);

        ParkingSpot result = parkingSpotService.update(spot);

        assertNotNull(result);
        assertEquals(spot.getSpotId(), result.getSpotId());

        verify(parkingSpotValidator).validate(spot);
        verify(parkingSpotDao).update(spot);
    }

    @Test
    void update_ShouldThrowException_WhenSpotIsNull() {

        ValidationException exception = assertThrows(ValidationException.class, () ->
                parkingSpotService.update(null));

        assertEquals("Parking spot must not be null", exception.getMessage());
        verifyNoInteractions(parkingSpotDao);
    }

    @Test
    void update_ShouldThrowException_WhenSpotIdIsNull() {

        ParkingSpot spot = buildParkingSpot();

        spot.setSpotId(null);

        ValidationException exception = assertThrows(ValidationException.class, () ->
                parkingSpotService.update(spot));

        assertEquals("Parking spot id is required", exception.getMessage());
        verifyNoInteractions(parkingSpotDao);
    }

    @Test
    void update_ShouldThrowException_WhenSpotArchived() {

        ParkingSpot spot = buildParkingSpot();

        ParkingSpot archivedSpot = buildParkingSpot();
        archivedSpot.setStatus(ParkingSpotStatus.ARCHIVED);

        when(parkingSpotDao.findById(1L)).thenReturn(Optional.of(archivedSpot));

        ValidationException exception = assertThrows(ValidationException.class, () ->
                parkingSpotService.update(spot));

        assertEquals("Archived parking spot cannot be updated", exception.getMessage());

        verify(parkingSpotValidator).validate(spot);
        verify(parkingSpotDao, never()).update(any());
    }

    @Test
    void deleteById_ShouldArchiveSpot() {

        ParkingSpot spot = buildParkingSpot();

        when(parkingSpotDao.findById(1L)).thenReturn(Optional.of(spot));

        when(parkingSpotDao.update(any(ParkingSpot.class))).thenReturn(spot);

        boolean result = parkingSpotService.deleteById(1L);

        assertTrue(result);

        assertEquals(ParkingSpotStatus.ARCHIVED, spot.getStatus());
        verify(parkingSpotDao).update(spot);
    }

    @Test
    void deleteById_ShouldReturnFalse_WhenAlreadyArchived() {

        ParkingSpot spot = buildParkingSpot();
        spot.setStatus(ParkingSpotStatus.ARCHIVED);

        when(parkingSpotDao.findById(1L)).thenReturn(Optional.of(spot));

        boolean result = parkingSpotService.deleteById(1L);

        assertFalse(result);

        verify(parkingSpotDao, never()).update(any());
    }

    @Test
    void deleteById_ShouldThrowException_WhenIdInvalid() {

        assertThrows(ValidationException.class, () ->
                parkingSpotService.deleteById(0L));

        verifyNoInteractions(parkingSpotDao);
    }

    @Test
    void findByOwnerId_ShouldReturnSpots() {

        List<ParkingSpot> spots = List.of(buildParkingSpot());

        when(parkingSpotDao.findByOwnerId(1L, 1, 10)).thenReturn(spots);

        List<ParkingSpot> result = parkingSpotService.findByOwnerId(1L, 1, 10);

        assertEquals(1, result.size());

        verify(parkingSpotDao).findByOwnerId(1L, 1, 10);
    }

    @Test
    void findByOwnerId_ShouldThrowException_WhenOwnerIdInvalid() {

        assertThrows(ValidationException.class, () ->
                parkingSpotService.findByOwnerId(0L, 1, 10));

        verifyNoInteractions(parkingSpotDao);
    }

    @Test
    void findAvailable_ShouldReturnAvailableSpots() {

        List<ParkingSpot> spots = List.of(buildParkingSpot());

        when(parkingSpotDao.findAvailable(1, 10)).thenReturn(spots);

        List<ParkingSpot> result = parkingSpotService.findAvailable(1, 10);

        assertEquals(1, result.size());

        verify(parkingSpotDao).findAvailable(1, 10);
    }

    @Test
    void findAvailable_ShouldThrowException_WhenPageInvalid() {

        assertThrows(ValidationException.class, () ->
                parkingSpotService.findAvailable(0, 10));

        verifyNoInteractions(parkingSpotDao);
    }
}
