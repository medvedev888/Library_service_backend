package me.vladislav.library_service_backend.loan.controller;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.security.CustomUserDetails;
import me.vladislav.library_service_backend.common.dto.ApiResponse;
import me.vladislav.library_service_backend.loan.dto.BookLoanDTO;
import me.vladislav.library_service_backend.loan.dto.ReserveBookRequest;
import me.vladislav.library_service_backend.loan.service.BookLoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class BookLoanController {

    private final BookLoanService bookLoanService;

    /**
     * Читатель: забронировать книгу
     * POST /loans/reserve
     */
    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse> reserveBook(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReserveBookRequest request
    ) {
        Long userId = userDetails.getUser().getId();
        BookLoanDTO loan = bookLoanService.reserveBook(userId, request.bookId(), request.libraryId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Книга успешно забронирована", loan));
    }

    /**
     * Читатель: отменить бронь (также может вызываться staff-страницей)
     * POST /loans/{id}/cancel
     */
    @PostMapping("/{loanId}/cancel")
    public ResponseEntity<ApiResponse> cancelReservation(@PathVariable Long loanId) {
        BookLoanDTO loan = bookLoanService.cancelReservation(loanId);
        return ResponseEntity.ok(ApiResponse.success("Бронь отменена", loan));
    }

    /**
     * Библиотекарь: подтвердить бронь
     * POST /loans/{id}/approve
     */
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/{loanId}/approve")
    public ResponseEntity<ApiResponse> approveReservation(@PathVariable Long loanId) {
        BookLoanDTO loan = bookLoanService.approveReservation(loanId);
        return ResponseEntity.ok(ApiResponse.success("Бронь подтверждена", loan));
    }

    /**
     * Библиотекарь: выдать книгу
     * POST /loans/{id}/issue
     */
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/{loanId}/issue")
    public ResponseEntity<ApiResponse> issueBook(@PathVariable Long loanId) {
        BookLoanDTO loan = bookLoanService.issueBook(loanId);
        return ResponseEntity.ok(ApiResponse.success("Книга выдана", loan));
    }

    /**
     * Библиотекарь: вернуть книгу
     * POST /loans/{id}/return
     */
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/{loanId}/return")
    public ResponseEntity<ApiResponse> returnBook(@PathVariable Long loanId) {
        BookLoanDTO loan = bookLoanService.returnBook(loanId);
        return ResponseEntity.ok(ApiResponse.success("Книга возвращена", loan));
    }

    // -------------------- READ endpoints (нужны фронту) --------------------

    /**
     * Читатель: список своих броней/выдач
     * GET /loans/my
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse> myLoans(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        List<BookLoanDTO> loans = bookLoanService.listMyLoans(userId);
        return ResponseEntity.ok(ApiResponse.success("Мои бронирования/выдачи", loans));
    }

    /**
     * Библиотекарь: список всех броней/выдач (для таблиц)
     * GET /loans?q=...&status=...
     * status ожидается как LoanStatus (PENDING/RESERVED/ISSUED/OVERDUE/RETURNED/CANCELLED)
     */
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping
    public ResponseEntity<ApiResponse> allLoans(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status
    ) {
        List<BookLoanDTO> loans = bookLoanService.listLoansForStaff(q, status);
        return ResponseEntity.ok(ApiResponse.success("Список бронирований/выдач", loans));
    }

    /**
     * Библиотекарь: получить бронь/выдачу по id (для CirculationPage)
     * GET /loans/{id}
     */
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/{loanId}")
    public ResponseEntity<ApiResponse> getLoan(@PathVariable Long loanId) {
        BookLoanDTO loan = bookLoanService.getLoanById(loanId);
        return ResponseEntity.ok(ApiResponse.success("Бронь/выдача", loan));
    }
}
