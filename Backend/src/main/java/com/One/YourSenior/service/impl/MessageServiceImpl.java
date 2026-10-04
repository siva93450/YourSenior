package com.One.YourSenior.service.impl;

import com.One.YourSenior.model.Booking;
import com.One.YourSenior.model.BookingStatus;
import com.One.YourSenior.model.Message;
import com.One.YourSenior.repository.BookingRepository;
import com.One.YourSenior.repository.MessageRepository;
import com.One.YourSenior.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Message sendMessage(Long bookingId, String senderRole, String content) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new RuntimeException("Messaging is only available for accepted bookings");
        }

        Message message = new Message();
        message.setBooking(booking);
        message.setSenderRole(senderRole);
        message.setContent(content);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessages(Long bookingId) {
        return messageRepository.findByBookingIdOrderBySentAtAsc(bookingId);
    }
}