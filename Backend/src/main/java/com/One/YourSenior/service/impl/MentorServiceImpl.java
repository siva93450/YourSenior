package com.One.YourSenior.service.impl;

import com.One.YourSenior.model.Mentor;
import com.One.YourSenior.repository.MentorRepository;
import com.One.YourSenior.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorServiceImpl implements MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mentor registerMentor(Mentor mentor) {
        if (mentorRepository.existsByEmail(mentor.getEmail())) {
            throw new RuntimeException("Mentor with this email already exists");
        }
        mentor.setPassword(passwordEncoder.encode(mentor.getPassword())); // hash before saving
        return mentorRepository.save(mentor);
    }

    @Override
    public Mentor getMentorById(Long id) {
        return mentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor not found with id: " + id));
    }

    @Override
    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }

    @Override
    public List<Mentor> searchMentors(String college, String course) {
        if (college != null && course != null) {
            return mentorRepository.findByCollegeAndCourse(college, course);
        } else if (college != null) {
            return mentorRepository.findByCollege(college);
        }
        return mentorRepository.findAll();
    }

    @Override
    public Mentor updateMentor(Long id, Mentor updatedMentor) {
        Mentor existing = getMentorById(id);
        existing.setName(updatedMentor.getName());
        existing.setCollege(updatedMentor.getCollege());
        existing.setCourse(updatedMentor.getCourse());
        existing.setGraduationYear(updatedMentor.getGraduationYear());
        existing.setBio(updatedMentor.getBio());
        existing.setAvailable(updatedMentor.isAvailable());
        return mentorRepository.save(existing);
    }

    @Override
    public void deleteMentor(Long id) {
        Mentor existing = getMentorById(id);
        mentorRepository.delete(existing);
    }

    @Override
    public Mentor validateCredentials(String email, String password) {
        Mentor mentor = mentorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!passwordEncoder.matches(password, mentor.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        return mentor;
    }



    // impl
    @Override
    public List<String> getDistinctColleges() {
        return mentorRepository.findDistinctColleges();
    }

    @Override
    public List<String> getDistinctCoursesByCollege(String college) {
        return mentorRepository.findDistinctCoursesByCollege(college);
    }
}