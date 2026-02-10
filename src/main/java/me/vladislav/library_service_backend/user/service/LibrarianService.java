package me.vladislav.library_service_backend.user.service;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.exception.UserNotFoundException;
import me.vladislav.library_service_backend.auth.model.Role;
import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.auth.repository.UserRepository;
import me.vladislav.library_service_backend.common.exception.ForbiddenActionException;
import me.vladislav.library_service_backend.library.exception.LibraryNotFoundException;
import me.vladislav.library_service_backend.library.model.Library;
import me.vladislav.library_service_backend.library.repository.LibraryRepository;
import me.vladislav.library_service_backend.user.dto.LibrarianDTO;
import me.vladislav.library_service_backend.user.exception.LibrarianNotFoundException;
import me.vladislav.library_service_backend.user.mapper.LibrarianMapper;
import me.vladislav.library_service_backend.user.model.Librarian;
import me.vladislav.library_service_backend.user.repository.LibrarianRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor

@Service
public class LibrarianService {
    private final LibrarianRepository librarianRepository;
    private final LibrarianMapper librarianMapper;
    private final UserRepository userRepository;
    private final LibraryRepository libraryRepository;

    @Transactional
    public LibrarianDTO create(LibrarianDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));

        Library library = null;
        if (dto.getLibraryId() != null) {
            library = libraryRepository.findById(dto.getLibraryId())
                    .orElseThrow(() -> new LibraryNotFoundException(dto.getLibraryId()));
        }

        Librarian librarian = librarianMapper.toEntity(dto);
        librarian.setUser(user);
        librarian.setLibrary(library);

        librarian = librarianRepository.save(librarian);
        return librarianMapper.toDto(librarian);
    }


    @Transactional
    public LibrarianDTO assignLibrary(Long librarianId, Long libraryId) {
        checkLibraryAccess(libraryId);

        Librarian librarian = librarianRepository.findById(librarianId)
                .orElseThrow(() -> new LibrarianNotFoundException(librarianId));

        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new LibraryNotFoundException(libraryId));

        librarian.setLibrary(library);

        librarian = librarianRepository.save(librarian);
        return librarianMapper.toDto(librarian);
    }


    @Transactional(readOnly = true)
    public void checkLibraryAccess(Long libraryId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!user.getRole().equals(Role.LIBRARIAN)) {
            throw new ForbiddenActionException("Только библиотекари могут выполнять это действие");
        }

        Librarian librarian = librarianRepository.findByUserId(user.getId())
                .orElseThrow(() -> new LibrarianNotFoundException(user.getId()));

        if (librarian.getLibrary() == null || !librarian.getLibrary().getId().equals(libraryId)) {
            throw new ForbiddenActionException("Доступ запрещён для этой библиотеки");
        }
    }

}
