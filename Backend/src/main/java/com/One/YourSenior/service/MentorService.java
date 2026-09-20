package com.One.YourSenior.service;

import com.One.YourSenior.model.Mentor;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface MentorService {
    Mentor registerMentor(Mentor mentor);
    Mentor getMentorById(Long id);
    List<Mentor> getAllMentors();
    List<Mentor> searchMentors(String college, String course);
    Mentor updateMentor(Long id, Mentor updatedMentor);
    void deleteMentor(Long id);
    Mentor validateCredentials(String email, String password);

    // interface
    List<String> getDistinctColleges();

    List<String> getDistinctCoursesByCollege(String college);
}