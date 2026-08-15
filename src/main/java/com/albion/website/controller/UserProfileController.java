package com.albion.website.controller;

import com.albion.website.dto.UserDto;
import com.albion.website.model.User;
import com.albion.website.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserProfileController {
    private final UserRepository userRepository;

    @GetMapping("/api/profile")
    public ResponseEntity<UserDto> getProfile(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));

        return ResponseEntity.ok(new UserDto(user.getName(), user.getEmail(), user.getCreateDate()));
    }
}