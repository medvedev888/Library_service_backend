package me.vladislav.library_service_backend.notification.dto;

public record ReservationApprovedEvent(Long loanId, String userEmail, String bookTitle) {}

