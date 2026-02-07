package me.vladislav.library_service_backend.loan.controller;


import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.security.CustomUserDetails;
import me.vladislav.library_service_backend.common.dto.ApiResponse;
import me.vladislav.library_service_backend.loan.dto.BookLoanDTO;
import me.vladislav.library_service_backend.loan.dto.ReserveBookRequest;
import me.vladislav.library_service_backend.loan.model.BookLoan;
import me.vladislav.library_service_backend.loan.service.BookLoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor

@RestController
@RequestMapping("/loans")
public class BookLoanController {

    private final BookLoanService bookLoanService;


    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse> reserveBook(@RequestBody ReserveBookRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        BookLoanDTO savedBookLoan = bookLoanService.reserveBook(
                userDetails.getUser().getId(),
                request.bookId(),
                request.libraryId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Бронь успешно оформлена", savedBookLoan));
    }
}
