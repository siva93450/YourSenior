package com.One.YourSenior.controller;

import com.One.YourSenior.model.Message;
import com.One.YourSenior.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/{bookingId}")
    public ResponseEntity<Message> send(
            @PathVariable Long bookingId,
            @RequestParam String senderRole,
            @RequestBody Map<String, String> body) {
        Message saved = messageService.sendMessage(bookingId, senderRole, body.get("content"));
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<List<Message>> getAll(@PathVariable Long bookingId) {
        return ResponseEntity.ok(messageService.getMessages(bookingId));
    }
}