package com.One.YourSenior.service.impl;

import com.One.YourSenior.model.*;
import com.One.YourSenior.repository.BookingRepository;
import com.One.YourSenior.repository.MentorRepository;
import com.One.YourSenior.repository.UserRepository;
import com.One.YourSenior.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Booking createBooking(Long userId, Long mentorId, Booking booking) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found with id: " + mentorId));

        if (!mentor.isAvailable()) {
            throw new RuntimeException("Mentor is currently not available for bookings");
        }

        booking.setUser(user);
        booking.setMentor(mentor);
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    @Override
    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> getBookingsByMentor(Long mentorId) {
        return bookingRepository.findByMentorId(mentorId);
    }

    @Override
    public Booking updateStatus(Long id, BookingStatus status) {
        Booking booking = getBookingById(id);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        Booking booking = getBookingById(id);
        bookingRepository.delete(booking);
    }

    @Autowired
    private MentorRepository mentorRepository;

    @Override
    public Booking rateBooking(Long bookingId, int ratingValue) {
        if (ratingValue < 1 || ratingValue > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        Booking booking = getBookingById(bookingId);

        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new RuntimeException("Only accepted bookings can be rated");
        }
        if (booking.isRated()) {
            throw new RuntimeException("This booking has already been rated");
        }

        Mentor mentor = booking.getMentor();
        double currentTotal = mentor.getAverageRating() * mentor.getRatingCount();
        int newCount = mentor.getRatingCount() + 1;
        double newAverage = (currentTotal + ratingValue) / newCount;

        mentor.setAverageRating(newAverage);
        mentor.setRatingCount(newCount);
        mentorRepository.save(mentor);

        booking.setRated(true);
        return bookingRepository.save(booking);
    }

}