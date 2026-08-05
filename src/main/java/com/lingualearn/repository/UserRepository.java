package com.lingualearn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lingualearn.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}