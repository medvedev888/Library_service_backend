package me.vladislav.library_service_backend.book.service;


import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.book.dto.BookDTO;
import me.vladislav.library_service_backend.book.exception.AuthorReferenceNotFoundException;
import me.vladislav.library_service_backend.book.exception.BookNotFoundException;
import me.vladislav.library_service_backend.book.exception.DuplicateBookException;
import me.vladislav.library_service_backend.book.mapper.BookMapper;
import me.vladislav.library_service_backend.book.model.Author;
import me.vladislav.library_service_backend.book.model.Book;
import me.vladislav.library_service_backend.book.repository.AuthorRepository;
import me.vladislav.library_service_backend.book.repository.BookRepository;
import me.vladislav.library_service_backend.common.exception.InvalidParameterException;
import me.vladislav.library_service_backend.library.exception.LibraryNotFoundException;
import me.vladislav.library_service_backend.library.model.Library;
import me.vladislav.library_service_backend.library.repository.LibraryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorRepository authorRepository;
    private final LibraryRepository libraryRepository;


    @Transactional(readOnly = true)
    public List<BookDTO> getAll(
            int page,
            int size,
            String sortBy,
            String sortDir,
            Map<String, String> filters
    ) {

        Set<String> allowedFields = Set.of(
                "title", "isbn", "genre", "language", "publishingHouse", "publicationYear"
        );

        if (!allowedFields.contains(sortBy)) {
            throw new InvalidParameterException("Невозможно сортировать по полю: " + sortBy);
        }

        for (String field : filters.keySet()) {
            if (!allowedFields.contains(field)) {
                throw new InvalidParameterException("Невозможно фильтровать по полю: " + field);
            }
        }

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        int pageIndex = page - 1;
        if (pageIndex < 0) pageIndex = 0;
        Pageable pageable = PageRequest.of(pageIndex, size, sort);

        Specification<Book> spec = (root, query, cb) -> cb.conjunction();

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%"));
        }

        Page<Book> bookPage = bookRepository.findAll(spec, pageable);
        return bookPage.stream().map(bookMapper::toDTO).toList();
    }


    @Transactional
    public BookDTO create(BookDTO bookDTO) {
        String isbn = bookDTO.getIsbn();
        if (bookRepository.existsByIsbn(isbn)) {
            throw new DuplicateBookException(isbn);
        }
        Book book = bookMapper.toEntity(bookDTO);
        Book res = bookRepository.save(book);
        return bookMapper.toDTO(res);
    }


    @Transactional
    public BookDTO update(String currentIsbn, BookDTO request) {

        Book book = bookRepository.findByIsbn(currentIsbn)
                .orElseThrow(() -> new BookNotFoundException(currentIsbn));

        if (request.getIsbn() != null && !request.getIsbn().equals(currentIsbn)) {
            if (bookRepository.existsByIsbn(request.getIsbn())) {
                throw new DuplicateBookException(request.getIsbn());
            }
            book.setIsbn(request.getIsbn());
        }

        book.setTitle(request.getTitle());
        book.setPublishingHouse(request.getPublishingHouse());
        book.setPublicationYear(request.getPublicationYear());
        book.setGenre(request.getGenre());
        book.setLanguage(request.getLanguage());

        if (request.getAuthorIds() != null) {
            Set<Author> authors = new HashSet<>(
                    authorRepository.findAllById(request.getAuthorIds())
            );
            if (authors.size() != request.getAuthorIds().size()) {
                throw new AuthorReferenceNotFoundException();
            }
            book.setAuthors(authors);
        }

        if (request.getLibraryIds() != null) {
            Set<Library> libraries = new HashSet<>(
                    libraryRepository.findAllById(request.getLibraryIds())
            );
            if (libraries.size() != request.getLibraryIds().size()) {
                throw new LibraryNotFoundException();
            }
            book.setLibraries(libraries);
        }

        return bookMapper.toDTO(book);
    }


    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

}
