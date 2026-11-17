package com.albion.website.config;

import com.albion.website.model.Role;
import com.albion.website.model.User;
import com.albion.website.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Creates (or promotes) an admin account on startup when ADMIN_USERNAME and
 * ADMIN_PASSWORD are provided. Does nothing when they are not set.
 * An existing user with that name is promoted to ADMIN; its password is left unchanged.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:}")
    private String username;

    @Value("${app.admin.email:}")
    private String email;

    @Value("${app.admin.password:}")
    private String password;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (username.isBlank() || password.isBlank()) {
            return;
        }

        userRepository.findByName(username).ifPresentOrElse(user -> {
            if (user.getRole() != Role.ADMIN) {
                user.setRole(Role.ADMIN);
                userRepository.save(user);
                log.info("Promoted existing user '{}' to ADMIN", username);
            }
        }, () -> {
            String mail = email.isBlank() ? username + "@admin.local" : email;
            if (userRepository.findByEmail(mail).isPresent()) {
                log.warn("Admin account not created: email {} is already used by another account", mail);
                return;
            }
            User admin = new User();
            admin.setName(username);
            admin.setEmail(mail);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            log.info("Created admin account '{}'", username);
        });
    }
}
