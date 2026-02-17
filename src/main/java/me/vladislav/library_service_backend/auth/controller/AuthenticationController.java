package me.vladislav.library_service_backend.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.dto.*;
import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.auth.security.CustomUserDetails;
import me.vladislav.library_service_backend.auth.service.AuthService;
import me.vladislav.library_service_backend.auth.service.PasswordResetService;
import me.vladislav.library_service_backend.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid SignUpRequest request) {
        String token = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Пользователь успешно зарегистрирован",
                new SignUpResponse(token))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid SignInRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok().body(ApiResponse.success(
                "Пользователь успешно авторизован",
                new SignInResponse(token))
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok().body(ApiResponse.success(
                "Данные пользователя",
                new UserResponse(user.getEmail(), user.getRole())
        ));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        passwordResetService.requestReset(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(
                "Если такой email существует, на него отправлена ссылка для сброса пароля",
                null
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success("Пароль успешно обновлён", null));
    }
}

