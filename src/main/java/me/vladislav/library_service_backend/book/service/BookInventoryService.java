package me.vladislav.library_service_backend.book.service;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.book.dto.BookInventoryDTO;
import me.vladislav.library_service_backend.book.exception.BookInventoryNotFoundException;
import me.vladislav.library_service_backend.book.exception.BookNotAvailableException;
import me.vladislav.library_service_backend.book.exception.BookNotFoundInInventoryException;
import me.vladislav.library_service_backend.book.exception.DuplicateBookInventoryException;
import me.vladislav.library_service_backend.book.mapper.BookInventoryMapper;
import me.vladislav.library_service_backend.book.model.Book;
import me.vladislav.library_service_backend.book.model.BookInventory;
import me.vladislav.library_service_backend.book.repository.BookInventoryRepository;
import me.vladislav.library_service_backend.book.repository.BookRepository;
import me.vladislav.library_service_backend.common.exception.InvalidParameterException;
import me.vladislav.library_service_backend.library.model.Library;
import me.vladislav.library_service_backend.library.repository.LibraryRepository;
import me.vladislav.library_service_backend.user.service.LibrarianService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class BookInventoryService {
    private final BookInventoryRepository bookInventoryRepository;
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final BookInventoryMapper bookInventoryMapper;
    private final LibrarianService librarianService;
    private final ObjectMapper objectMapper;


    @Transactional(readOnly = true)
    public List<BookInventoryDTO> getAll(
            int page,
            int size,
            String sortBy,
            String sortDir,
            Map<String, String> filters
    ) {

        Set<String> allowedFields = Set.of(
                "totalCopies",
                "availableCopies",
                "id"
        );

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

        int pageIndex = page - 1;
        if (pageIndex < 0) pageIndex = 0;

        Pageable pageable = PageRequest.of(pageIndex, size, sort);

        Specification<BookInventory> spec = (root, query, cb) -> cb.conjunction();

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String field = entry.getKey();
            Long value = Long.valueOf(entry.getValue());

            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get(field), value)
            );
        }

        Page<BookInventory> pageResult = bookInventoryRepository.findAll(spec, pageable);
        return pageResult.stream()
                .map(bookInventoryMapper::toDTO)
                .toList();
    }


    @Transactional
    public BookInventoryDTO create(BookInventoryDTO dto) {
        librarianService.checkLibraryAccess(dto.getLibraryId());

        boolean exists = bookInventoryRepository.existsByBookIdAndLibraryId(
                dto.getBookId(),
                dto.getLibraryId()
        );

        if (exists) {
            throw new DuplicateBookInventoryException(
                    dto.getBookId(),
                    dto.getLibraryId()
            );
        }

        if (dto.getAvailableCopies() > dto.getTotalCopies()) {
            throw new InvalidParameterException(
                    "Количество доступных копий не может быть больше общего количества копий"
            );
        }

        BookInventory entity = bookInventoryMapper.toEntity(dto);
        BookInventory saved = bookInventoryRepository.save(entity);
        return bookInventoryMapper.toDTO(saved);
    }


    @Transactional
    public BookInventoryDTO update(BookInventoryDTO dto) {
        BookInventory inventory = bookInventoryRepository.findById(dto.getId())
                .orElseThrow(() -> new BookInventoryNotFoundException(dto.getId()));

        librarianService.checkLibraryAccess(inventory.getLibrary().getId());

        boolean countsChanged =
                !Objects.equals(inventory.getTotalCopies(), dto.getTotalCopies()) ||
                        !Objects.equals(inventory.getAvailableCopies(), dto.getAvailableCopies());

        if (countsChanged) {
            if (dto.getAvailableCopies() > dto.getTotalCopies()) {
                throw new InvalidParameterException(
                        "availableCopies не может быть больше totalCopies"
                );
            }

            inventory.setTotalCopies(dto.getTotalCopies());
            inventory.setAvailableCopies(dto.getAvailableCopies());
        }

        BookInventory updated = bookInventoryRepository.save(inventory);
        return bookInventoryMapper.toDTO(updated);
    }


    @Transactional
    public void delete(Long id) {
        BookInventory bookInventory = bookInventoryRepository.findById(id)
                .orElseThrow(() -> new BookInventoryNotFoundException(id));

        librarianService.checkLibraryAccess(bookInventory.getLibrary().getId());

        bookInventoryRepository.deleteById(id);
    }


    @Transactional
    public void importBookInventoriesFromJson(MultipartFile file) {
        try {
            List<BookInventoryDTO> inventories = objectMapper.readValue(
                    file.getInputStream(),
                    new TypeReference<List<BookInventoryDTO>>() {
                    }
            );

            for (BookInventoryDTO dto : inventories) {
                if (!bookRepository.existsById(dto.getBookId())) {
                    throw new InvalidParameterException("Книга с id " + dto.getBookId() + " не найдена");
                }
                if (!libraryRepository.existsById(dto.getLibraryId())) {
                    throw new InvalidParameterException("Библиотека с id " + dto.getLibraryId() + " не найдена");
                }

                BookInventory entity = bookInventoryMapper.toEntity(dto);
                bookInventoryRepository.save(entity);
            }
        } catch (IOException e) {
            throw new InvalidParameterException("Не удалось прочитать файл инвентаря: " + e.getMessage());
        }
    }


    @Transactional(propagation = Propagation.MANDATORY)
    public void decreaseAvailableCopies(Book book, Library library) {
        BookInventory bookInventory = bookInventoryRepository
                .findByBookAndLibrary(book, library)
                .orElseThrow(() -> new BookNotFoundInInventoryException("Книга не найдена в библиотеке"));


        if (bookInventory.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("Нет доступных экземпляров книги");
        }

        bookInventory.setAvailableCopies(bookInventory.getAvailableCopies() - 1);
        bookInventoryRepository.save(bookInventory);
    }


    @Transactional(propagation = Propagation.MANDATORY)
    public void increaseAvailableCopies(Book book, Library library) {
        BookInventory bookInventory = bookInventoryRepository
                .findByBookAndLibrary(book, library)
                .orElseThrow(() -> new BookNotFoundInInventoryException("Книга не найдена в библиотеке"));

        bookInventory.setAvailableCopies(bookInventory.getAvailableCopies() + 1);
        bookInventoryRepository.save(bookInventory);
    }

}
