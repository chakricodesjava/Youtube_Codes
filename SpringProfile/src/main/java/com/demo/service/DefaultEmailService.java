package com.demo.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("default")
public class DefaultEmailService implements EmailService{
    @Override
    public String getMessages() {
        return "Fetching emails using DefaultEmailService";
    }
}
