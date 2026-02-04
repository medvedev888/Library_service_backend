package me.vladislav.library_service_backend.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.book.dto.BookInventoryDTO;
import me.vladislav.library_service_backend.book.dto.CreateBookInventoryRequest;
import me.vladislav.library_service_backend.book.dto.UpdateBookInventoryRequest;
import me.vladislav.library_service_backend.book.mapper.BookInventoryMapper;
import me.vladislav.library_service_backend.book.service.BookInventoryService;
import me.vladislav.library_service_backend.common.dto.ApiResponse;
import me.vladislav.library_service_backend.common.exception.InvalidParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/book-inventories")
public class BookInventoryController {
    private final BookInventoryService bookInventoryService;
    private final BookInventoryMapper bookInventoryMapper;


    @GetMapping
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Map<String, String> allParams
    ) {
        Map<String, String> filters = allParams.entrySet().stream()
                .filter(e -> e.getKey().startsWith("filter."))
                .collect(Collectors.toMap(e -> e.getKey().substring(7), Map.Entry::getValue));

        List<BookInventoryDTO> bookInventoryDTOS = bookInventoryService.getAll(page, size, sortBy, sortDir, filters);
        return ResponseEntity.ok(ApiResponse.success("Список инвентарей книг", bookInventoryDTOS));
    }


    @PostMapping
    public ResponseEntity<ApiResponse> create(
            @RequestBody @Valid CreateBookInventoryRequest request
    ) {
        BookInventoryDTO bookInventoryDTO = bookInventoryMapper.toDTO(request);
        BookInventoryDTO savedBookInventory = bookInventoryService.create(bookInventoryDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Инвентарь книги создан", savedBookInventory));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBookInventoryRequest request
    ) {
        if (id < 0) {
            throw new InvalidParameterException("id не может быть отрицательным");
        }
        BookInventoryDTO bookInventoryDTO = bookInventoryMapper.toDTO(request);
        bookInventoryDTO.setId(id);
        BookInventoryDTO savedBookInventory = bookInventoryService.update(bookInventoryDTO);
        return ResponseEntity.ok(ApiResponse.success("Инвентарь книги обновлён", savedBookInventory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        if (id < 0) {
            throw new InvalidParameterException("id не может быть отрицательным");
        }
        bookInventoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Инвентарь книги удалён", null));
    }
}

