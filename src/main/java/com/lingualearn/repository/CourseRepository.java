package com.lingualearn.repository;

import com.lingualearn.model.Course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByLanguageAndDifficulty(
            String language,
            String difficulty,
            Pageable pageable);

    Page<Course> findByLanguage(
            String language,
            Pageable pageable);

    Page<Course> findByDifficulty(
            String difficulty,
            Pageable pageable);
}