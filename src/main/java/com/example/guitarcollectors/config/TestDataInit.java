package com.example.guitarcollectors.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.guitarcollectors.model.User;
import com.example.guitarcollectors.repository.UserRepository;

@Component
public class TestDataInit implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder pwdEncoder;

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(new User("user", pwdEncoder.encode("pwd"), Collections.singletonList("ROLE_USER")));
        userRepository.save(new User("admin", pwdEncoder.encode("pwd"), Collections.singletonList("ROLE_ADMIN")));
    }

}
