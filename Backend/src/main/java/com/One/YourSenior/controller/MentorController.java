package com.One.YourSenior.controller;

import com.One.YourSenior.dto.LoginRequest;
import com.One.YourSenior.dto.LoginResponse;
import com.One.YourSenior.model.Mentor;
import com.One.YourSenior.security.JwtUtil;
import com.One.YourSenior.service.MentorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentors")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Mentor> register(@Valid @RequestBody Mentor mentor) {
        Mentor saved = mentorService.registerMentor(mentor);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Mentor mentor = mentorService.validateCredentials(request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(mentor.getEmail(), "MENTOR");
        return ResponseEntity.ok(new LoginResponse(token, "MENTOR", mentor.getId(), mentor.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mentor> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mentorService.getMentorById(id));
    }

    @GetMapping
    public ResponseEntity<List<Mentor>> getAll() {
        return ResponseEntity.ok(mentorService.getAllMentors());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Mentor>> search(
            @RequestParam(required = false) String college,
            @RequestParam(required = false) String course) {
        return ResponseEntity.ok(mentorService.searchMentors(college, course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mentor> update(@PathVariable Long id, @RequestBody Mentor mentor) {
        return ResponseEntity.ok(mentorService.updateMentor(id, mentor));
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<Mentor> setAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {
        Mentor mentor = mentorService.getMentorById(id);
        mentor.setAvailable(available);
        return ResponseEntity.ok(mentorService.updateMentor(id, mentor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mentorService.deleteMentor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/colleges")
    public ResponseEntity<List<String>> getColleges() {
        return ResponseEntity.ok(mentorService.getDistinctColleges());
    }

    @GetMapping("/colleges/{college}/courses")
    public ResponseEntity<List<String>> getCoursesByCollege(@PathVariable String college) {
        return ResponseEntity.ok(mentorService.getDistinctCoursesByCollege(college));
    }
}