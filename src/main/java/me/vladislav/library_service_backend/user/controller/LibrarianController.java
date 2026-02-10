package me.vladislav.library_service_backend.user.controller;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.security.CustomUserDetails;
import me.vladislav.library_service_backend.common.dto.ApiResponse;
import me.vladislav.library_service_backend.user.dto.LibrarianDTO;
import me.vladislav.library_service_backend.user.service.LibrarianService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor

@RestController
@RequestMapping("/librarians")
public class LibrarianController {
    private final LibrarianService librarianService;

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PatchMapping("/{id}/assign-library")
    public ResponseEntity<ApiResponse> assignLibrary(
            @PathVariable("id") Long librarianId,
            @RequestParam("libraryId") Long libraryId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        LibrarianDTO updatedLibrarian = librarianService.assignLibrary(
                librarianId,
                libraryId,
                userDetails.getUser()
        );

        return ResponseEntity.ok().body(ApiResponse.success(
                "Библиотекарь успешно обновлен",
                updatedLibrarian
        ));
    }

}

