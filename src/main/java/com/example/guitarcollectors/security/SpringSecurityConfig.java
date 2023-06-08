package com.example.guitarcollectors.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

        @Bean
        public InMemoryUserDetailsManager userDetailsManager() {
                UserDetails user = User.withDefaultPasswordEncoder()
                                .username("user")
                                .password("pwd")
                                .roles("USER")
                                .build();

                UserDetails admin = User.withDefaultPasswordEncoder()
                                .username("admin")
                                .password("pwd")
                                .roles("ADMIN")
                                .build();

                UserDetails manager = User.withDefaultPasswordEncoder()
                                .username("manager")
                                .password("pwd")
                                .roles("MANAGER")
                                .build();

                UserDetails salesman = User.withDefaultPasswordEncoder()
                                .username("salesman")
                                .password("pwd")
                                .roles("SALESMAN")
                                .build();

                return new InMemoryUserDetailsManager(user, admin, manager, salesman);
        }

        @Bean
        public SecurityFilterChain configure(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> {
                                        auth.requestMatchers("/").permitAll();
                                        auth.requestMatchers(HttpMethod.GET, "/api/**").permitAll();
                                        auth.requestMatchers("/user").hasRole("USER");
                                        auth.requestMatchers("/admin").hasRole("ADMIN");
                                        auth.requestMatchers("api/warehouse/**", "api/sales/**").hasRole("SALESMAN");
                                        auth.requestMatchers("api/expense-items/**", "api/charges/**")
                                                        .hasRole("MANAGER");
                                })
                                .httpBasic(Customizer.withDefaults());
                return http.build();
        }

}