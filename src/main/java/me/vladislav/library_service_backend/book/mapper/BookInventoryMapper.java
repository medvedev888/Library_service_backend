package me.vladislav.library_service_backend.book.mapper;

import me.vladislav.library_service_backend.book.dto.BookInventoryDTO;
import me.vladislav.library_service_backend.book.dto.CreateBookInventoryRequest;
import me.vladislav.library_service_backend.book.dto.UpdateBookInventoryRequest;
import me.vladislav.library_service_backend.book.model.Book;
import me.vladislav.library_service_backend.book.model.BookInventory;
import me.vladislav.library_service_backend.library.model.Library;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookInventoryMapper {
    @Mapping(target = "id", ignore = true)
    BookInventoryDTO toDTO(CreateBookInventoryRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookId", ignore = true)
    @Mapping(target = "libraryId", ignore = true)
    BookInventoryDTO toDTO(UpdateBookInventoryRequest request);

    @Mapping(target = "book", expression = "java(mapBookId(dto.getBookId()))")
    @Mapping(target = "library", expression = "java(mapLibraryId(dto.getLibraryId()))")
    BookInventory toEntity(BookInventoryDTO dto);

    @Mapping(target = "bookId", expression = "java(mapBook(inventory.getBook()))")
    @Mapping(target = "libraryId", expression = "java(mapLibrary(inventory.getLibrary()))")
    BookInventoryDTO toDTO(BookInventory inventory);

    default Book mapBookId(Long id) {
        if (id == null) return null;
        Book b = new Book();
        b.setId(id);
        return b;
    }

    default Library mapLibraryId(Long id) {
        if (id == null) return null;
        Library l = new Library();
        l.setId(id);
        return l;
    }

    default Long mapBook(Book book) {
        return book == null ? null : book.getId();
    }

    default Long mapLibrary(Library library) {
        return library == null ? null : library.getId();
    }
}
