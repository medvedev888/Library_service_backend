package me.vladislav.library_service_backend.common.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.library_service_backend.auth.exception.AuthException;
import me.vladislav.library_service_backend.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Ошибки auth
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse> handleAuthException(AuthException ex) {
        log.warn("Auth error: {}", ex.getMessage());
        ApiResponse body = ApiResponse.error("Ошибка аутентификации", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    // Ошибки валидации полей @Valid (binding result)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        ApiResponse body = ApiResponse.error("Ошибка валидации входных данных", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Ошибки валидации @Constraint (например @RequestParam, @PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.toList());

        ApiResponse body = ApiResponse.error("Ошибка валидации параметров", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDenied(AuthorizationDeniedException ex) {

        ApiResponse body = ApiResponse.error(
                "У вас нет прав для выполнения этого действия",
                        List.of(ex.getMessage())
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    // Кастомные исключения всего приложения
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException ex) {

        ApiResponse body = ApiResponse.error(
                ex.getError(),
                List.of(ex.getMessage())
        );

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    // Все остальные RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Unexpected runtime exception", ex);
        ApiResponse body = ApiResponse.error("Непредвиденная ошибка", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
