package com.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.repository.AppUserRepository;

@SpringBootApplication
@RestController
public class SpringsecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringsecurityApplication.class, args);
    }

    // Ensure H2 in-memory database is initialized at startup
    @Bean
    public CommandLineRunner initDb(AppUserRepository userRepository) {
        return args -> {
            userRepository.count(); // Triggers JPA and initializes the DB
        };
    }

    @GetMapping("/hello")
    public String demo() {
        return "Hello World";
    }
}
