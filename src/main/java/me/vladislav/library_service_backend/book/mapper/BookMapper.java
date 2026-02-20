package me.vladislav.library_service_backend.book.mapper;

import me.vladislav.library_service_backend.book.dto.BookDTO;
import me.vladislav.library_service_backend.book.dto.CreateBookRequest;
import me.vladislav.library_service_backend.book.dto.UpdateBookRequest;
import me.vladislav.library_service_backend.book.model.Author;
import me.vladislav.library_service_backend.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    BookDTO toDTO(CreateBookRequest request);

    @Mapping(target = "id", ignore = true)
    BookDTO toDTO(UpdateBookRequest request);

    @Mapping(target = "authors", expression = "java(mapAuthorIds(dto.getAuthorIds()))")
    Book toEntity(BookDTO dto);

    @Mapping(target = "authorIds", expression = "java(mapAuthors(book.getAuthors()))")
    BookDTO toDTO(Book book);

    default Set<Author> mapAuthorIds(Set<Long> ids) {
        return ids.stream().map(id -> {
            Author a = new Author();
            a.setId(id);
            return a;
        }).collect(Collectors.toSet());
    }

    default Set<Long> mapAuthors(Set<Author> authors) {
        return authors.stream().map(Author::getId).collect(Collectors.toSet());
    }
}