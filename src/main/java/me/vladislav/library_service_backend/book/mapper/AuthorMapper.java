package me.vladislav.library_service_backend.book.mapper;

import me.vladislav.library_service_backend.book.dto.AuthorDTO;
import me.vladislav.library_service_backend.book.dto.CreateAuthorRequest;
import me.vladislav.library_service_backend.book.dto.UpdateAuthorRequest;
import me.vladislav.library_service_backend.book.model.Author;
import me.vladislav.library_service_backend.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookIds", ignore = true)
    AuthorDTO toDTO(CreateAuthorRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookIds", ignore = true)
    AuthorDTO toDTO(UpdateAuthorRequest request);

    @Mapping(target = "books", expression = "java(mapBookIds(dto.getBookIds()))")
    Author toEntity(AuthorDTO dto);

    @Mapping(target = "bookIds", expression = "java(mapBooks(author.getBooks()))")
    AuthorDTO toDTO(Author author);

    default Set<Book> mapBookIds(Set<Long> ids) {
        if (ids == null) {
            return Collections.emptySet();
        }
        return ids.stream().map(id -> {
            Book b = new Book();
            b.setId(id);
            return b;
        }).collect(Collectors.toSet());
    }

    default Set<Long> mapBooks(Set<Book> books) {
        if (books == null) {
            return Collections.emptySet();
        }
        return books.stream().map(Book::getId).collect(Collectors.toSet());
    }
}
