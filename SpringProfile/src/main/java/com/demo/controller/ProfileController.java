package com.demo.controller;

import com.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Autowired
    private EmailService emailService;

    @GetMapping("/profile")
    public String getActiveProfile() {
        return " " + "Active profile: " + activeProfile +
                "\n Email : " +
                emailService.getMessages() +
                "\n";
    }
}
