package com.One.YourSenior.controller;

import com.One.YourSenior.model.Booking;
import com.One.YourSenior.model.BookingStatus;
import com.One.YourSenior.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<Booking> create(
            @RequestParam Long userId,
            @RequestParam Long mentorId,
            @RequestBody Booking booking) {
        Booking saved = bookingService.createBooking(userId, mentorId, booking);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<Booking>> getByMentor(@PathVariable Long mentorId) {
        return ResponseEntity.ok(bookingService.getBookingsByMentor(mentorId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Booking> updateStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status) {
        return ResponseEntity.ok(bookingService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/rate")
    public ResponseEntity<Booking> rate(
            @PathVariable Long id,
            @RequestParam int rating) {
        return ResponseEntity.ok(bookingService.rateBooking(id, rating));
    }
}