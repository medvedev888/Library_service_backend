package me.vladislav.library_service_backend.library.service;


import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.common.exception.InvalidParameterException;
import me.vladislav.library_service_backend.library.dto.LibraryDTO;
import me.vladislav.library_service_backend.library.exception.DuplicateLibraryException;
import me.vladislav.library_service_backend.library.exception.LibraryNotFoundException;
import me.vladislav.library_service_backend.library.mapper.LibraryMapper;
import me.vladislav.library_service_backend.library.model.Library;
import me.vladislav.library_service_backend.library.repository.LibraryRepository;
import me.vladislav.library_service_backend.user.service.LibrarianService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final LibraryMapper libraryMapper;
    private final LibrarianService librarianService;


    @Transactional(readOnly = true)
    public List<LibraryDTO> getAll(
            int page,
            int size,
            String sortBy,
            String sortDir,
            Map<String, String> filters
    ) {

        Set<String> allowedFields = Set.of("staffNumber", "status", "id");

        if (!allowedFields.contains(sortBy)) {
            throw new InvalidParameterException("Невозможно сортировать по полю: " + sortBy);
        }

        for (String field : filters.keySet()) {
            if (!allowedFields.contains(field)) {
                throw new InvalidParameterException("Невозможно фильтровать по полю: " + field);
            }
        }

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        int pageIndex = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(pageIndex, size, sort);

        Specification<Library> spec = (root, query, cb) -> cb.conjunction();

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get(entry.getKey()), entry.getValue())
            );
        }

        Page<Library> libraryPage = libraryRepository.findAll(spec, pageable);
        return libraryPage.stream()
                .map(libraryMapper::toDTO)
                .toList();
    }


    @Transactional
    public LibraryDTO create(LibraryDTO libraryDTO) {
        boolean exists = libraryRepository.existsByAddress(libraryDTO.getAddress());
        if (exists) {
            throw new DuplicateLibraryException(
                    libraryDTO.getAddress().toString()
            );
        }

        Library library = libraryMapper.toEntity(libraryDTO);
        Library saved = libraryRepository.save(library);
        return libraryMapper.toDTO(saved);
    }


    @Transactional
    public LibraryDTO update(LibraryDTO libraryDTO) {
        librarianService.checkLibraryAccess(libraryDTO.getId());

        Library library = libraryRepository.findById(libraryDTO.getId())
                .orElseThrow(() -> new LibraryNotFoundException(libraryDTO.getId()));

        if (!library.getAddress().equals(libraryDTO.getAddress())) {
            boolean exists = libraryRepository.existsByAddress(libraryDTO.getAddress());
            if (exists) {
                throw new DuplicateLibraryException(
                        libraryDTO.getAddress().toString()
                );
            }
            library.setAddress(libraryDTO.getAddress());
        }

        library.setStaffNumber(libraryDTO.getStaffNumber());
        library.setStatus(libraryDTO.getStatus());

        Library updated = libraryRepository.save(library);
        return libraryMapper.toDTO(updated);
    }


    @Transactional
    public void delete(Long id) {
        librarianService.checkLibraryAccess(id);
        if (!libraryRepository.existsById(id)) {
            throw new LibraryNotFoundException(id);
        }
        libraryRepository.deleteById(id);
    }
}

