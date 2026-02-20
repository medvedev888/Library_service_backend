package me.vladislav.library_service_backend.book.service;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.book.dto.AuthorDTO;
import me.vladislav.library_service_backend.book.exception.AuthorNotFoundException;
import me.vladislav.library_service_backend.book.exception.DuplicateAuthorException;
import me.vladislav.library_service_backend.book.mapper.AuthorMapper;
import me.vladislav.library_service_backend.book.model.Author;
import me.vladislav.library_service_backend.book.repository.AuthorRepository;
import me.vladislav.library_service_backend.common.exception.InvalidParameterException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final ObjectMapper objectMapper;


    @Transactional(readOnly = true)
    public List<AuthorDTO> getAll(
            int page,
            int size,
            String sortBy,
            String sortDir,
            Map<String, String> filters
    ) {

        Set<String> allowedFields = Set.of("surname", "name", "middleName");

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

        Specification<Author> spec = (root, query, cb) -> cb.conjunction();

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%"));
        }

        Page<Author> authorPage = authorRepository.findAll(spec, pageable);
        return authorPage.stream().map(authorMapper::toDTO).toList();
    }


    @Transactional
    public AuthorDTO create(AuthorDTO authorDTO) {
        boolean exists = authorRepository.existsBySurnameAndNameAndMiddleName(
                authorDTO.getSurname(),
                authorDTO.getName(),
                authorDTO.getMiddleName()
        );
        if (exists) {
            throw new DuplicateAuthorException(authorDTO.getSurname(), authorDTO.getName(), authorDTO.getMiddleName());
        }
        Author author = authorMapper.toEntity(authorDTO);
        Author saved = authorRepository.save(author);
        return authorMapper.toDTO(saved);
    }


    @Transactional
    public AuthorDTO update(AuthorDTO authorDTO) {
        Author author = authorRepository.findById(authorDTO.getId())
                .orElseThrow(() -> new AuthorNotFoundException(
                        authorDTO.getName(),
                        authorDTO.getMiddleName(),
                        authorDTO.getSurname()
                ));

        boolean fullNameChanged = !author.getSurname().equals(authorDTO.getSurname()) ||
                !author.getName().equals(authorDTO.getName()) ||
                !Objects.equals(author.getMiddleName(), authorDTO.getMiddleName());

        if (fullNameChanged) {
            boolean exists = authorRepository.existsBySurnameAndNameAndMiddleName(
                    authorDTO.getSurname(),
                    authorDTO.getName(),
                    authorDTO.getMiddleName()
            );
            if (exists) {
                throw new DuplicateAuthorException(authorDTO.getSurname(), authorDTO.getName(), authorDTO.getMiddleName());
            }
            author.setSurname(authorDTO.getSurname());
            author.setName(authorDTO.getName());
            author.setMiddleName(authorDTO.getMiddleName());
        }

        Author updated = authorRepository.save(author);
        return authorMapper.toDTO(updated);
    }


    @Transactional
    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException(id);
        }
        authorRepository.deleteById(id);
    }


    @Transactional
    public void importAuthorsFromJson(MultipartFile file) {
        try {
            List<AuthorDTO> authors = objectMapper.readValue(file.getInputStream(), new TypeReference<>() {});
            for (AuthorDTO dto : authors) {
                Author entity = authorMapper.toEntity(dto);
                authorRepository.save(entity);
            }
        } catch (IOException e) {
            throw new InvalidParameterException("Не удалось прочитать файл авторов: " + e.getMessage());
        }
    }

}
