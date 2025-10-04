package com.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringProfileApplication implements CommandLineRunner {

    public static void main(String[] args) {
		SpringApplication.run(SpringProfileApplication.class, args);
	}

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Spring Application Started...  Active Profile is:  "+activeProfile);
    }
}
