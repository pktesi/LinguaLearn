package com.lingualearn.controller;

import com.lingualearn.model.Course;
import com.lingualearn.repository.CourseRepository;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses")
    public String showCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String difficulty,
            Model model) {

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Course> coursePage;

        boolean hasLanguage =
                language != null && !language.isBlank();

        boolean hasDifficulty =
                difficulty != null && !difficulty.isBlank();

        if (hasLanguage && hasDifficulty) {
            coursePage =
                    courseRepository.findByLanguageAndDifficulty(
                            language,
                            difficulty,
                            pageable);
        } else if (hasLanguage) {
            coursePage =
                    courseRepository.findByLanguage(
                            language,
                            pageable);
        } else if (hasDifficulty) {
            coursePage =
                    courseRepository.findByDifficulty(
                            difficulty,
                            pageable);
        } else {
            coursePage = courseRepository.findAll(pageable);
        }

        model.addAttribute(
                "courses",
                coursePage.getContent());

        model.addAttribute(
                "coursePage",
                coursePage);

        model.addAttribute(
                "currentPage",
                page);

        model.addAttribute(
                "totalPages",
                coursePage.getTotalPages());

        model.addAttribute(
                "selectedLanguage",
                language);

        model.addAttribute(
                "selectedDifficulty",
                difficulty);

        model.addAttribute(
                "sortField",
                sortField);

        model.addAttribute(
                "sortDirection",
                sortDirection);

        return "courses";
    }

    @GetMapping("/courses/new")
    public String showCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "course-form";
    }

    @PostMapping("/courses")
    public String saveCourse(
            @Valid @ModelAttribute("course") Course course,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "course-form";
        }

        courseRepository.save(course);

        return "redirect:/courses";
    }
}