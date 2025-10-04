package com.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestApiController {

    @GetMapping("/public/hello")
    public Map<String, String> publicHello() {
        return Map.of("message", "Hello from public API");
    }

    @GetMapping("/user/me")
    public Map<String, String> me(@AuthenticationPrincipal UserDetails ud) {
        return Map.of("user", ud.getUsername());
    }

    @GetMapping("/admin/secret")
    public Map<String, String> adminOnly() {
        return Map.of("secret", "Top secret data for ADMIN only");
    }
}