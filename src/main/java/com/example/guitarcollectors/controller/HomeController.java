package com.example.guitarcollectors.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, world";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Hello, admin";
    }

    @GetMapping("/user")
    public String user() {
        return "Hello, user";
    }
}
