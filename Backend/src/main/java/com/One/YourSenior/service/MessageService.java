package com.One.YourSenior.service;

import com.One.YourSenior.model.Message;
import java.util.List;

public interface MessageService {
    Message sendMessage(Long bookingId, String senderRole, String content);
    List<Message> getMessages(Long bookingId);
}