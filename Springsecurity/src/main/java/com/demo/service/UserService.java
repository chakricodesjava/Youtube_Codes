package com.demo.service;

import com.demo.model.AppUser;
import com.demo.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or #username == authentication.principal.username")
    public AppUser getByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @PreAuthorize("authentication.principal.username == #user.username")
    public void updateUser(AppUser user) {
        userRepository.save(user);
    }

    @PostAuthorize("returnObject != null && returnObject.username == authentication.principal.username")
    public AppUser findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}