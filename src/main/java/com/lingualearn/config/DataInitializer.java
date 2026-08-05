package com.lingualearn.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lingualearn.model.AppUser;
import com.lingualearn.model.Role;
import com.lingualearn.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // Create Admin
            if (!userRepository.existsByEmail("admin@lingualearn.com")) {

                AppUser admin = new AppUser();
                admin.setFullName("LinguaLearn Administrator");
                admin.setEmail("admin@lingualearn.com");
                admin.setPassword(passwordEncoder.encode("Admin123!"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("Default administrator account created.");
            }

            // Create Instructor
            if (!userRepository.existsByEmail("instructor@lingualearn.com")) {

                AppUser instructor = new AppUser();
                instructor.setFullName("John Instructor");
                instructor.setEmail("instructor@lingualearn.com");
                instructor.setPassword(passwordEncoder.encode("Instructor123!"));
                instructor.setRole(Role.INSTRUCTOR);

                userRepository.save(instructor);

                System.out.println("Default instructor account created.");
            }

        };
    }
}