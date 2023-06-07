package com.example.guitarcollectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@SpringBootApplication
public class GuitarcollectorsApplication {
	public static void main(String[] args) {
		SpringApplication.run(GuitarcollectorsApplication.class, args);
	}

	@Bean
	public PasswordEncoder PasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
