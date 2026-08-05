package com.lingualearn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.lingualearn.model.Course;
import com.lingualearn.repository.CourseRepository;

@Controller
public class AdminController {

    private final CourseRepository courseRepository;

    public AdminController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/admin/courses")
    public String showAdminCourses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "admin/courses";
    }

    @GetMapping("/admin/courses/edit/{id}")
    public String showEditCourseForm(
            @PathVariable Long id,
            Model model) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid course ID: " + id
                        )
                );

        model.addAttribute("course", course);
        return "admin/edit-course";
    }

    @PostMapping("/admin/courses/edit/{id}")
    public String updateCourse(
            @PathVariable Long id,
            Course editedCourse) {

        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid course ID: " + id
                        )
                );

        existingCourse.setTitle(editedCourse.getTitle());
        existingCourse.setDescription(editedCourse.getDescription());
        existingCourse.setLanguage(editedCourse.getLanguage());
        existingCourse.setDifficulty(editedCourse.getDifficulty());
        existingCourse.setInstructorName(editedCourse.getInstructorName());
        existingCourse.setDurationWeeks(editedCourse.getDurationWeeks());
        existingCourse.setPrice(editedCourse.getPrice());

        courseRepository.save(existingCourse);

        return "redirect:/admin/courses";
    }

    @PostMapping("/admin/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {

        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        }

        return "redirect:/admin/courses";
    }
}