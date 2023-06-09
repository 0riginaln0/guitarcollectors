package com.example.guitarcollectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.guitarcollectors.config.JwtConfigProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtConfigProperties.class)
public class GuitarcollectorsApplication {
	public static void main(String[] args) {
		SpringApplication.run(GuitarcollectorsApplication.class, args);
	}
}