package com.demo.config;

import com.demo.model.AppUser;
import com.demo.model.Role;
import com.demo.repository.AppUserRepository;
import com.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataLoader {
    private final RoleRepository roleRepo;
    private final AppUserRepository userRepo;
    private final PasswordEncoder encoder;


    @Bean
    CommandLineRunner send(){
        return args -> {
            Role userRole = roleRepo.findByName("USER").orElseGet(() -> roleRepo.save(Role.builder().name("USER").build()));
            Role adminRole = roleRepo.findByName("ADMIN").orElseGet(() -> roleRepo.save(Role.builder().name("ADMIN").build()));
            Role actAdminRole = roleRepo.findByName("ACTUATOR_ADMIN").orElseGet(() -> roleRepo.save(Role.builder().name("ACTUATOR_ADMIN").build()));

            if (userRepo.findByUsername("admin").isEmpty()) {
                userRepo.save(AppUser.builder()
                        .username("admin")
                        .password(encoder.encode("admin"))
                        .enabled(true)
                        .roles(Set.of(userRole, adminRole, actAdminRole))
                        .build());
            }

            if (userRepo.findByUsername("user").isEmpty()) {
                userRepo.save(AppUser.builder()
                        .username("user")
                        .password(encoder.encode("password"))
                        .enabled(true)
                        .roles(Set.of(userRole))
                        .build());
            }
        };
    }
}
