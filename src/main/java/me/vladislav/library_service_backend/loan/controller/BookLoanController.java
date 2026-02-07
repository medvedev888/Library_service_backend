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

@RequiredArgsConstructor

@RestController
@RequestMapping("/loans")
public class BookLoanController {

    private final BookLoanService bookLoanService;


    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse> reserveBook(@RequestBody ReserveBookRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        BookLoanDTO bookLoan = bookLoanService.reserveBook(
                userDetails.getUser().getId(),
                request.bookId(),
                request.libraryId()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Бронь успешно оформлена", bookLoan));
    }


    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/{loanId}/approve")
    public ResponseEntity<ApiResponse> approve(@PathVariable Long loanId) {
        BookLoanDTO bookLoan = bookLoanService.approveReservation(loanId);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("Бронь успешно подтверждена", bookLoan));
    }


    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/{loanId}/issue")
    public ResponseEntity<ApiResponse> issueBook(@PathVariable Long loanId) {
        BookLoanDTO bookLoan = bookLoanService.issueBook(loanId);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("Книга успешно выдана", bookLoan));
    }


    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/{loanId}/return")
    public ResponseEntity<ApiResponse> returnBook(@PathVariable Long loanId) {
        BookLoanDTO bookLoan = bookLoanService.returnBook(loanId);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("Книга успешно возвращена", bookLoan));
    }


    @PostMapping("/{loanId}/cancel")
    public ResponseEntity<ApiResponse> cancelReservation(@PathVariable Long loanId) {
        BookLoanDTO bookLoan = bookLoanService.cancelReservation(loanId);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("Бронь успешно отменена", bookLoan));
    }

}
