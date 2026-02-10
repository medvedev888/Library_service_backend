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
import me.vladislav.library_service_backend.loan.exception.*;
import me.vladislav.library_service_backend.loan.mapper.BookLoanMapper;
import me.vladislav.library_service_backend.loan.model.BookLoan;
import me.vladislav.library_service_backend.loan.model.LoanStatus;
import me.vladislav.library_service_backend.loan.repository.BookLoanRepository;
import me.vladislav.library_service_backend.user.service.LibrarianService;
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
    private final LibrarianService librarianService;


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


    @Transactional
    public BookLoanDTO approveReservation(Long loanId) {
        BookLoan bookLoan = bookLoanRepository.findById(loanId)
                .orElseThrow(() -> new BookLoanNotFoundException(loanId));

        librarianService.checkLibraryAccess(bookLoan.getLibrary().getId());

        if (bookLoan.getStatus() != LoanStatus.PENDING) {
            throw new BookLoanStateException(
                    HttpStatus.CONFLICT,
                    "Бронь не может быть подтверждена в текущем статусе"
            );
        }

        bookLoan.setStatus(LoanStatus.RESERVED);

        bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(bookLoan);
    }


    @Transactional
    public BookLoanDTO issueBook(Long loanId) {
        BookLoan bookLoan = bookLoanRepository.findById(loanId)
                .orElseThrow(() -> new BookLoanNotFoundException(loanId));

        if (bookLoan.getStatus() != LoanStatus.RESERVED) {
            throw new BookLoanStateException(
                    HttpStatus.CONFLICT,
                    "Книгу можно выдать только по подтверждённой брони"
            );
        }

        bookLoan.setStatus(LoanStatus.ISSUED);
        bookLoan.setIssuedAt(LocalDateTime.now());
        bookLoan.setDueAt(LocalDateTime.now().plusMonths(3));

        bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(bookLoan);
    }


    @Transactional
    public BookLoanDTO returnBook(Long loanId) {
        BookLoan bookLoan = bookLoanRepository.findById(loanId)
                .orElseThrow(() -> new BookLoanNotFoundException(loanId));

        if (bookLoan.getStatus() != LoanStatus.ISSUED && bookLoan.getStatus() != LoanStatus.OVERDUE) {
            throw new BookLoanStateException(
                    HttpStatus.CONFLICT,
                    "Вернуть можно только выданную или просроченную книгу"
            );
        }

        bookLoan.setStatus(LoanStatus.RETURNED);
        bookLoan.setReturnedAt(LocalDateTime.now());

        try {
            bookInventoryService.increaseAvailableCopies(bookLoan.getBook(), bookLoan.getLibrary());
        } catch (BookNotFoundInInventoryException ex) {
            throw new BookLoanReturnException(ex.getStatus(), ex.getMessage());
        }

        return bookLoanMapper.toDTO(bookLoan);
    }


    @Transactional
    public BookLoanDTO cancelReservation(Long loanId) {
        BookLoan bookLoan = bookLoanRepository.findById(loanId)
                .orElseThrow(() -> new BookLoanNotFoundException(loanId));

        if (bookLoan.getStatus() != LoanStatus.PENDING && bookLoan.getStatus() != LoanStatus.RESERVED) {
            throw new BookLoanStateException(
                    HttpStatus.CONFLICT,
                    "Отменить можно только ожидающую или подтверждённую бронь"
            );
        }

        bookLoan.setStatus(LoanStatus.CANCELLED);

        try {
            bookInventoryService.increaseAvailableCopies(bookLoan.getBook(), bookLoan.getLibrary());
        } catch (BookNotFoundInInventoryException ex) {
            throw new BookLoanReturnException(ex.getStatus(), ex.getMessage());
        }

        return bookLoanMapper.toDTO(bookLoan);
    }

}
