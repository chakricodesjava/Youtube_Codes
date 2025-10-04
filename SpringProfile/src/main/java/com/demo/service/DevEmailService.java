package com.demo.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DevEmailService implements EmailService{
    @Override
    public String getMessages() {
        return "Fetching emails using DevEmailService";
    }
}
