package com.One.YourSenior.repository;

import com.One.YourSenior.model.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {

    Optional<Mentor> findByEmail(String email);
    boolean existsByEmail(String email);

    List<Mentor> findByCollegeAndCourse(String college, String course);
    List<Mentor> findByCollege(String college);

    @Query("SELECT DISTINCT m.college FROM Mentor m")
    List<String> findDistinctColleges();

    @Query("SELECT DISTINCT m.course FROM Mentor m WHERE m.college = :college")
    List<String> findDistinctCoursesByCollege(String college);
}