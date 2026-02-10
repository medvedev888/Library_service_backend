package me.vladislav.library_service_backend.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.common.dto.ApiResponse;
import me.vladislav.library_service_backend.common.exception.InvalidParameterException;
import me.vladislav.library_service_backend.library.dto.CreateLibraryRequest;
import me.vladislav.library_service_backend.library.dto.LibraryDTO;
import me.vladislav.library_service_backend.library.dto.UpdateLibraryRequest;
import me.vladislav.library_service_backend.library.mapper.LibraryMapper;
import me.vladislav.library_service_backend.library.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@RestController
@RequestMapping("/libraries")
public class LibraryController {
    private final LibraryService libraryService;
    private final LibraryMapper libraryMapper;


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
                .collect(Collectors.toMap(
                        e -> e.getKey().substring(7),
                        Map.Entry::getValue
                ));

        List<LibraryDTO> libraries =
                libraryService.getAll(page, size, sortBy, sortDir, filters);

        return ResponseEntity.ok(
                ApiResponse.success("Список библиотек", libraries)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(
            @RequestBody @Valid CreateLibraryRequest request
    ) {
        LibraryDTO dto = libraryMapper.toDTO(request);
        LibraryDTO saved = libraryService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Библиотека успешно добавлена", saved));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateLibraryRequest request
    ) {
        if (id < 0) {
            throw new InvalidParameterException("id не может быть отрицательным");
        }

        LibraryDTO dto = libraryMapper.toDTO(request);
        dto.setId(id);

        LibraryDTO updated = libraryService.update(dto);

        return ResponseEntity.ok(
                ApiResponse.success("Библиотека успешно обновлена", updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        if (id < 0) {
            throw new InvalidParameterException("id не может быть отрицательным");
        }

        libraryService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Библиотека успешно удалена", null)
        );
    }
}
