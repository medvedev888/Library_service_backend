package me.vladislav.library_service_backend.library.mapper;

import me.vladislav.library_service_backend.book.model.Book;
import me.vladislav.library_service_backend.library.dto.CreateLibraryRequest;
import me.vladislav.library_service_backend.library.dto.LibraryDTO;
import me.vladislav.library_service_backend.library.dto.UpdateLibraryRequest;
import me.vladislav.library_service_backend.library.model.Library;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LibraryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookIds", ignore = true)
    LibraryDTO toDTO(CreateLibraryRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookIds", ignore = true)
    LibraryDTO toDTO(UpdateLibraryRequest request);

    @Mapping(target = "books", expression = "java(mapBookIds(dto.getBookIds()))")
    Library toEntity(LibraryDTO dto);

    @Mapping(target = "bookIds", expression = "java(mapBooks(library.getBooks()))")
    LibraryDTO toDTO(Library library);

    default Set<Book> mapBookIds(Set<Long> ids) {
        if (ids == null) {
            return Collections.emptySet();
        }
        return ids.stream()
                .map(id -> {
                    Book book = new Book();
                    book.setId(id);
                    return book;
                })
                .collect(Collectors.toSet());
    }

    default Set<Long> mapBooks(Set<Book> books) {
        if (books == null) {
            return Collections.emptySet();
        }
        return books.stream()
                .map(Book::getId)
                .collect(Collectors.toSet());
    }
}
