package com.One.YourSenior.service;

import com.One.YourSenior.model.Booking;
import com.One.YourSenior.model.BookingStatus;
import java.util.List;

public interface BookingService {
    Booking createBooking(Long userId, Long mentorId, Booking booking);
    Booking getBookingById(Long id);
    List<Booking> getBookingsByUser(Long userId);
    List<Booking> getBookingsByMentor(Long mentorId);
    Booking updateStatus(Long id, BookingStatus status);
    void deleteBooking(Long id);
    Booking rateBooking(Long bookingId, int ratingValue);
}