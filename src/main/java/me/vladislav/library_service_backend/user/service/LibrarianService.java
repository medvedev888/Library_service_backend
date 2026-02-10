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
    public LibrarianDTO create(LibrarianDTO librarianDTO) {
        User user = userRepository.findById(librarianDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException(librarianDTO.getUserId()));

        Library library = null;
        if (librarianDTO.getLibraryId() != null) {
            library = libraryRepository.findById(librarianDTO.getLibraryId())
                    .orElseThrow(() -> new LibraryNotFoundException(librarianDTO.getLibraryId()));
        }

        Librarian librarian = librarianMapper.toEntity(librarianDTO);
        librarian.setUser(user);
        librarian.setLibrary(library);

        librarian = librarianRepository.save(librarian);
        return librarianMapper.toDto(librarian);
    }


    @Transactional
    public LibrarianDTO assignLibrary(Long librarianId, Long libraryId, User currentUser) {
        if (!currentUser.getRole().equals(Role.LIBRARIAN)) {
            throw new ForbiddenActionException("Только библиотекари могут назначать библиотеку");
        }

        Librarian currentLibrarian = librarianRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new LibrarianNotFoundException(currentUser.getId()));

        if (!currentLibrarian.getLibrary().getId().equals(libraryId)) {
            throw new ForbiddenActionException("Нельзя назначить библиотеку, к которой вы не принадлежите");
        }

        Librarian librarian = librarianRepository.findById(librarianId)
                .orElseThrow(() -> new LibrarianNotFoundException(librarianId));

        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new LibraryNotFoundException(libraryId));

        librarian.setLibrary(library);

        librarian = librarianRepository.save(librarian);
        return librarianMapper.toDto(librarian);
    }

}
