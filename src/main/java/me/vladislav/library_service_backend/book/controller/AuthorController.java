package me.vladislav.library_service_backend.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.book.dto.AuthorDTO;
import me.vladislav.library_service_backend.book.dto.CreateAuthorRequest;
import me.vladislav.library_service_backend.book.dto.UpdateAuthorRequest;
import me.vladislav.library_service_backend.book.mapper.AuthorMapper;
import me.vladislav.library_service_backend.book.service.AuthorService;
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
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorMapper authorMapper;


    @GetMapping
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "surname") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Map<String, String> allParams
    ) {
        Map<String, String> filters = allParams.entrySet().stream()
                .filter(e -> e.getKey().startsWith("filter."))
                .collect(Collectors.toMap(e -> e.getKey().substring(7), Map.Entry::getValue));

        List<AuthorDTO> authors = authorService.getAll(page, size, sortBy, sortDir, filters);
        return ResponseEntity.ok(ApiResponse.success("Список авторов", authors));
    }


    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody @Valid CreateAuthorRequest request) {
        AuthorDTO authorDTO = authorMapper.toDTO(request);
        AuthorDTO savedAuthor = authorService.create(authorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Автор успешно добавлен", savedAuthor));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateAuthorRequest request
    ) {
        if (id < 0) {
            throw new InvalidParameterException("id не может быть отрицательным");
        }
        AuthorDTO authorDTO = authorMapper.toDTO(request);
        authorDTO.setId(id);
        AuthorDTO updatedAuthor = authorService.update(authorDTO);
        return ResponseEntity.ok().body(ApiResponse.success("Автор успешно обновлен", updatedAuthor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        if (id < 0) {
            throw new InvalidParameterException("id не может быть отрицательным");
        }
        authorService.delete(id);
        return ResponseEntity.ok().body(ApiResponse.success("Автор успешно удалён", null));
    }
}
