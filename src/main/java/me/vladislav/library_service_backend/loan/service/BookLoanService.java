package me.vladislav.library_service_backend.loan.service;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.auth.repository.UserRepository;
import me.vladislav.library_service_backend.book.exception.BookNotAvailableException;
import me.vladislav.library_service_backend.book.exception.BookNotFoundInInventoryException;
import me.vladislav.library_service_backend.book.model.Book;
import me.vladislav.library_service_backend.book.repository.BookRepository;
import me.vladislav.library_service_backend.book.service.BookInventoryService;
import me.vladislav.library_service_backend.library.model.Library;
import me.vladislav.library_service_backend.library.repository.LibraryRepository;
import me.vladislav.library_service_backend.loan.dto.BookLoanDTO;
import me.vladislav.library_service_backend.loan.exception.BookLoanReservationException;
import me.vladislav.library_service_backend.loan.exception.BookReferenceNotFoundException;
import me.vladislav.library_service_backend.loan.exception.LibraryReferenceNotFoundException;
import me.vladislav.library_service_backend.loan.exception.UserReferenceNotFoundException;
import me.vladislav.library_service_backend.loan.mapper.BookLoanMapper;
import me.vladislav.library_service_backend.loan.model.BookLoan;
import me.vladislav.library_service_backend.loan.model.LoanStatus;
import me.vladislav.library_service_backend.loan.repository.BookLoanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor

@Service
public class BookLoanService {

    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final BookLoanRepository bookLoanRepository;
    private final BookInventoryService bookInventoryService;
    private final BookLoanMapper bookLoanMapper;


    @Transactional
    public BookLoanDTO reserveBook(Long userId, Long bookId, Long libraryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserReferenceNotFoundException(userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookReferenceNotFoundException(bookId));

        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new LibraryReferenceNotFoundException(libraryId));

        boolean exists = bookLoanRepository.existsByUserAndBookAndLibraryAndStatusIn(
                user, book, library, List.of(LoanStatus.PENDING, LoanStatus.RESERVED, LoanStatus.ISSUED));
        if (exists) {
            throw new BookLoanReservationException(HttpStatus.CONFLICT, "Вы уже забронировали эту книгу в этой библиотеке");
        }

        BookLoan bookLoan = BookLoan.builder()
                .user(user)
                .book(book)
                .library(library)
                .status(LoanStatus.PENDING)
                .reservedAt(LocalDateTime.now())
                .reservedUntil(LocalDateTime.now().plusDays(7))
                .build();

        try {
            bookInventoryService.decreaseAvailableCopies(book, library);
        } catch (BookNotFoundInInventoryException | BookNotAvailableException ex) {
            throw new BookLoanReservationException(ex.getStatus(), ex.getMessage());
        }

        bookLoan = bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(bookLoan);
    }

    public void approveReservation(Long loanId) {

    }

    public void issueBook(Long loanId) {

    }

    public void returnBook(Long loanId) {

    }

    public void cancelReservation(Long loanId) {

    }

}
