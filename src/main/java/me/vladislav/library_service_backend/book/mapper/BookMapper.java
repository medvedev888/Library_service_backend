package me.vladislav.library_service_backend.book.mapper;

import me.vladislav.library_service_backend.book.dto.BookDTO;
import me.vladislav.library_service_backend.book.dto.CreateBookRequest;
import me.vladislav.library_service_backend.book.dto.UpdateBookRequest;
import me.vladislav.library_service_backend.book.model.Author;
import me.vladislav.library_service_backend.book.model.Book;
import me.vladislav.library_service_backend.library.model.Library;
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
    @Mapping(target = "libraries", expression = "java(mapLibraryIds(dto.getLibraryIds()))")
    Book toEntity(BookDTO dto);

    @Mapping(target = "authorIds", expression = "java(mapAuthors(book.getAuthors()))")
    @Mapping(target = "libraryIds", expression = "java(mapLibraries(book.getLibraries()))")
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

    default Set<Library> mapLibraryIds(Set<Long> ids) {
        return ids.stream().map(id -> {
            Library l = new Library();
            l.setId(id);
            return l;
        }).collect(Collectors.toSet());
    }

    default Set<Long> mapLibraries(Set<Library> libraries) {
        return libraries.stream().map(Library::getId).collect(Collectors.toSet());
    }

}
