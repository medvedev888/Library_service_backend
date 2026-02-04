package me.vladislav.library_service_backend.book.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.book.dto.BookDTO;
import me.vladislav.library_service_backend.book.dto.CreateBookRequest;
import me.vladislav.library_service_backend.book.dto.UpdateBookRequest;
import me.vladislav.library_service_backend.book.mapper.BookMapper;
import me.vladislav.library_service_backend.book.service.BookService;
import me.vladislav.library_service_backend.common.dto.ApiResponse;
import me.vladislav.library_service_backend.common.exception.InvalidParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;


    @GetMapping
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Map<String, String> allParams
    ) {
        Map<String, String> filters = allParams.entrySet().stream()
                .filter(e -> e.getKey().startsWith("filter."))
                .collect(Collectors.toMap(e -> e.getKey().substring(7), Map.Entry::getValue));

        List<BookDTO> books = bookService.getAll(page, size, sortBy, sortDir, filters);
        return ResponseEntity.ok(ApiResponse.success("Список книг", books));
    }


    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody @Valid CreateBookRequest request) {
        BookDTO bookDTO = bookMapper.toDTO(request);
        BookDTO savedBook = bookService.create(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Книга успешно добавлена", savedBook));
    }


    @PreAuthorize("hasRole('LIBRARIAN')")
    @PatchMapping("/{isbn}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable String isbn,
            @RequestBody @Valid UpdateBookRequest request
    ) {
        BookDTO bookDTO = bookMapper.toDTO(request);
        BookDTO updatedBook = bookService.update(isbn, bookDTO);
        return ResponseEntity.ok().body(ApiResponse.success("Книга успешно обновлена", updatedBook));
    }


    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        if (id < 0) {
            throw new InvalidParameterException("id не может быть отрицательным");
        }
        bookService.delete(id);
        return ResponseEntity.ok().body(ApiResponse.success("Книга успешно удалена", null));
    }
}
