package me.vladislav.library_service_backend.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.dto.*;
import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.auth.security.CustomUserDetails;
import me.vladislav.library_service_backend.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SignUpResponse> register(@RequestBody @Valid SignUpRequest request) {
        String token = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SignUpResponse("User registered successfully", token));
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> login(@RequestBody @Valid SignInRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new SignInResponse("Authorization was successful", token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        UserResponse response = new UserResponse(user.getEmail(), user.getRole());
        return ResponseEntity.ok(response);
    }

}

