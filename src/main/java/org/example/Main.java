package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * This class starts the Spring Boot application and executes code at startup
 * to encode a password using the BCrypt algorithm. The encoded password is
 * printed to the console.
 */




@SpringBootApplication
@EntityScan(basePackages = "org.example.entity")
@EnableJpaRepositories(basePackages = "org.example.repository")


public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String password = "password";
            String encodedPassword = encoder.encode(password);
            System.out.println(encodedPassword);
        };
    }
}


