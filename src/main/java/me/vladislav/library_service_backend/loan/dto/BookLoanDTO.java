package me.vladislav.library_service_backend.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.vladislav.library_service_backend.loan.model.LoanStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookLoanDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private Long libraryId;
    private LoanStatus status;
    private LocalDateTime reservedAt;
    private LocalDateTime reservedUntil;
    private LocalDateTime issuedAt;
    private LocalDateTime dueAt;
    private LocalDateTime returnedAt;
}

