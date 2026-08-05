package com.lingualearn.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.lingualearn.model.AppUser;
import com.lingualearn.model.Role;
import com.lingualearn.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("appUser", new AppUser());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("appUser") AppUser appUser,
            BindingResult bindingResult) {

        if (userRepository.existsByEmail(appUser.getEmail())) {
            bindingResult.rejectValue(
                    "email",
                    "duplicate.email",
                    "An account already exists with this email"
            );
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        // All users who register publicly begin as students.
        appUser.setRole(Role.STUDENT);

        userRepository.save(appUser);

        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}