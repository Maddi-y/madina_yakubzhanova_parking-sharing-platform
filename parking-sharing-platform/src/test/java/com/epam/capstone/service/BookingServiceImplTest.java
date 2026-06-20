package com.epam.capstone.service;

import com.epam.capstone.dao.BookingDao;
import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.BookingStatus;
import com.epam.capstone.service.impl.BookingServiceImpl;
import com.epam.capstone.validation.BookingValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingDao bookingDao;

    @Mock
    private BookingValidator bookingValidator;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking buildBooking() {

        User driver = new User();
        driver.setUserId(1L);

        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setSpotId(10L);

        Booking booking = new Booking();

        booking.setBookingId(100L);
        booking.setDriver(driver);
        booking.setParkingSpot(parkingSpot);
        booking.setStartTime(LocalDateTime.now().plusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(3));
        booking.setTotalPrice(BigDecimal.valueOf(2000));
        booking.setStatus(BookingStatus.PENDING);

        return booking;
    }

    @Test
    void create_ShouldSaveBooking_WhenDataIsValid() {

        Booking booking = buildBooking();

        when(bookingDao.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.create(booking);

        assertNotNull(result);

        assertEquals(BookingStatus.PENDING, result.getStatus());

        verify(bookingValidator).validate(booking);

        verify(bookingDao).save(booking);
    }

    @Test
    void create_ShouldThrowException_WhenBookingIsNull() {

        assertThrows(ValidationException.class, () -> bookingService.create(null));
        verifyNoInteractions(bookingDao);
    }

    @Test
    void create_ShouldThrowException_WhenStartTimeInPast() {

        Booking booking = buildBooking();

        booking.setStartTime(LocalDateTime.now().minusHours(1));

        assertThrows(ValidationException.class, () -> bookingService.create(booking));

        verify(bookingValidator).validate(booking);
        verify(bookingDao, never()).save(any());
    }

    @Test
    void findById_ShouldReturnBooking_WhenBookingExists() {

        Booking booking = buildBooking();

        when(bookingDao.findById(100L)).thenReturn(Optional.of(booking));

        Booking result = bookingService.findById(100L);

        assertNotNull(result);
        assertEquals(booking.getBookingId(), result.getBookingId());

        verify(bookingDao).findById(100L);
    }

    @Test
    void findById_ShouldThrowException_WhenIdIsNull() {

        assertThrows(ValidationException.class, () -> bookingService.findById(null));
        verifyNoInteractions(bookingDao);
    }

    @Test
    void findById_ShouldThrowException_WhenIdIsNegative() {

        assertThrows(ValidationException.class, () -> bookingService.findById(-1L));
        verifyNoInteractions(bookingDao);
    }

    @Test
    void findById_ShouldThrowException_WhenBookingNotFound() {

        when(bookingDao.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> bookingService.findById(100L));
        verify(bookingDao).findById(100L);
    }

    @Test
    void findAll_ShouldReturnBookings() {

        List<Booking> bookings = List.of(buildBooking(), buildBooking());

        when(bookingDao.findAll(1, 10)).thenReturn(bookings);

        List<Booking> result = bookingService.findAll(1, 10);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(bookingDao).findAll(1, 10);
    }

    @Test
    void findAll_ShouldThrowException_WhenPageInvalid() {

        assertThrows(ValidationException.class, () ->
                bookingService.findAll(0, 10)
        );

        verifyNoInteractions(bookingDao);
    }

    @Test
    void findAll_ShouldThrowException_WhenSizeInvalid() {

        assertThrows(ValidationException.class, () ->
                bookingService.findAll(1, 0)
        );

        verifyNoInteractions(bookingDao);
    }

    @Test
    void update_ShouldUpdateBooking_WhenValid() {

        Booking booking = buildBooking();

        when(bookingDao.findById(booking.getBookingId())).thenReturn(Optional.of(booking));
        when(bookingDao.update(any(Booking.class))).thenAnswer(
                invocation -> invocation.getArgument(0));

        Booking result = bookingService.update(booking);

        assertNotNull(result);
        assertEquals(booking.getBookingId(), result.getBookingId());

        verify(bookingValidator).validate(booking);
        verify(bookingDao).update(booking);
    }

    @Test
    void update_ShouldThrowException_WhenBookingIsNull() {

        assertThrows(
                ValidationException.class, () -> bookingService.update(null)
        );

        verifyNoInteractions(bookingDao);
    }

    @Test
    void update_ShouldThrowException_WhenBookingIdIsNull() {

        Booking booking = buildBooking();

        booking.setBookingId(null);

        assertThrows(ValidationException.class, () -> bookingService.update(booking));
        verifyNoInteractions(bookingDao);
    }

    @Test
    void update_ShouldThrowException_WhenBookingIsCancelled() {

        Booking booking = buildBooking();

        booking.setStatus(BookingStatus.CANCELLED);

        when(bookingDao.findById(booking.getBookingId())).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.update(booking));

        verify(bookingValidator).validate(booking);
        verify(bookingDao, never()).update(any());
    }

    @Test
    void update_ShouldThrowException_WhenBookingIsCompleted() {

        Booking booking = buildBooking();

        booking.setStatus(BookingStatus.COMPLETED);

        when(bookingDao.findById(booking.getBookingId())).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.update(booking));

        verify(bookingValidator).validate(booking);
        verify(bookingDao, never()).update(any());
    }

    @Test
    void update_ShouldThrowException_WhenStartTimeInPast() {

        Booking booking = buildBooking();

        booking.setStartTime(LocalDateTime.now().minusHours(1));

        when(bookingDao.findById(booking.getBookingId())).thenReturn(Optional.of(buildBooking()));

        assertThrows(ValidationException.class, () -> bookingService.update(booking));

        verify(bookingDao, never()).update(any());
    }

    @Test
    void cancel_ShouldCancelPendingBooking() {

        Booking booking = buildBooking();

        booking.setStatus(BookingStatus.PENDING);

        when(bookingDao.findById(booking.getBookingId())).thenReturn(Optional.of(booking));
        when(bookingDao.update(any(Booking.class))).thenAnswer(
                invocation -> invocation.getArgument(0));

        boolean result = bookingService.cancel(booking.getBookingId());

        assertTrue(result);
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());

        verify(bookingDao).update(booking);
    }

    @Test
    void cancel_ShouldCancelConfirmedBooking() {

        Booking booking = buildBooking();

        booking.setStatus(BookingStatus.CONFIRMED);

        when(bookingDao.findById(booking.getBookingId())).thenReturn(Optional.of(booking));
        when(bookingDao.update(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = bookingService.cancel(booking.getBookingId());

        assertTrue(result);
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());

        verify(bookingDao).update(booking);
    }

    @Test
    void cancel_ShouldReturnFalse_WhenAlreadyCancelled() {

        Booking booking = buildBooking();

        booking.setStatus(BookingStatus.CANCELLED);

        when(bookingDao.findById(booking.getBookingId())).thenReturn(Optional.of(booking));

        boolean result = bookingService.cancel(booking.getBookingId());

        assertFalse(result);

        verify(bookingDao, never()).update(any());
    }

    @Test
    void cancel_ShouldThrowException_WhenBookingCompleted() {

        Booking booking = buildBooking();

        booking.setStatus(BookingStatus.COMPLETED);

        when(bookingDao.findById(booking.getBookingId())).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () ->
                bookingService.cancel(booking.getBookingId())
        );

        verify(bookingDao, never()).update(any());
    }

    @Test
    void cancel_ShouldThrowException_WhenIdIsNull() {

        assertThrows(ValidationException.class, () ->
                bookingService.cancel(null)
        );

        verifyNoInteractions(bookingDao);
    }

    @Test
    void cancel_ShouldThrowException_WhenIdIsNegative() {

        assertThrows(ValidationException.class, () ->
                bookingService.cancel(-1L)
        );

        verifyNoInteractions(bookingDao);
    }

    @Test
    void findByDriverId_ShouldReturnBookings() {

        List<Booking> bookings = List.of(buildBooking(), buildBooking());

        when(bookingDao.findByDriverId(1L, 1, 10)).thenReturn(bookings);

        List<Booking> result = bookingService.findByDriverId(1L, 1, 10);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(bookingDao).findByDriverId(1L, 1, 10);
    }

    @Test
    void findByDriverId_ShouldThrowException_WhenDriverIdInvalid() {

        assertThrows(ValidationException.class, () ->
                bookingService.findByDriverId(0L, 1, 10)
        );

        verifyNoInteractions(bookingDao);
    }

    @Test
    void findByDriverId_ShouldThrowException_WhenPageInvalid() {

        assertThrows(ValidationException.class, () ->
                bookingService.findByDriverId(1L, 0, 10)
        );

        verifyNoInteractions(bookingDao);
    }

    @Test
    void findByDriverId_ShouldThrowException_WhenSizeInvalid() {

        assertThrows(ValidationException.class, () ->
                bookingService.findByDriverId(1L, 1, 0)
        );

        verifyNoInteractions(bookingDao);
    }

    @Test
    void findByParkingSpotId_ShouldReturnBookings() {

        List<Booking> bookings = List.of(buildBooking(), buildBooking());

        when(bookingDao.findByParkingSpotId(10L, 1, 10)).thenReturn(bookings);

        List<Booking> result = bookingService.findByParkingSpotId(10L, 1, 10);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(bookingDao).findByParkingSpotId(10L, 1, 10);
    }

    @Test
    void findByParkingSpotId_ShouldThrowException_WhenSpotIdInvalid() {

        assertThrows(ValidationException.class, () ->
                bookingService.findByParkingSpotId(0L, 1, 10)
        );

        verifyNoInteractions(bookingDao);
    }

    @Test
    void findByStatus_ShouldReturnBookings() {

        List<Booking> bookings = List.of(buildBooking(), buildBooking());

        when(bookingDao.findByStatus(BookingStatus.PENDING, 1, 10)).thenReturn(bookings);

        List<Booking> result = bookingService.findByStatus(BookingStatus.PENDING, 1, 10);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(bookingDao).findByStatus(BookingStatus.PENDING, 1, 10);
    }

    @Test
    void findByStatus_ShouldThrowException_WhenStatusNull() {

        assertThrows(ValidationException.class, () ->
                bookingService.findByStatus(null, 1, 10)
        );
        verifyNoInteractions(bookingDao);
    }

    @Test
    void findByStatus_ShouldThrowException_WhenPageInvalid() {

        assertThrows(ValidationException.class, () ->
                bookingService.findByStatus(BookingStatus.PENDING, 0, 10)
        );
        verifyNoInteractions(bookingDao);
    }

    @Test
    void findByStatus_ShouldThrowException_WhenSizeInvalid() {

        assertThrows(ValidationException.class, () ->
                bookingService.findByStatus(BookingStatus.PENDING, 1, 0)
        );
        verifyNoInteractions(bookingDao);
    }
}
