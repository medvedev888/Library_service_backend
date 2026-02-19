package me.vladislav.library_service_backend.notification.dto;

import java.math.BigDecimal;

public record FineCreatedEvent(Long loanId, String userEmail, String bookTitle, BigDecimal amount) {}
