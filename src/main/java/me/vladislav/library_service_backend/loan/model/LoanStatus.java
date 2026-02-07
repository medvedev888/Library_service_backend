package me.vladislav.library_service_backend.loan.model;

public enum LoanStatus {
    PENDING,
    RESERVED,
    ISSUED,
    RETURNED,
    CANCELLED,
    OVERDUE
}