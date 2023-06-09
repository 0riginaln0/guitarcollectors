package com.example.guitarcollectors.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.guitarcollectors.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
