package com.demo.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class ProdEmailService implements EmailService{
    @Override
    public String getMessages() {
       return "Sending email using ProdEmailService";
    }
}
