package me.vladislav.library_service_backend.loan.mapper;

import me.vladislav.library_service_backend.loan.dto.BookLoanDTO;
import me.vladislav.library_service_backend.loan.model.BookLoan;
import me.vladislav.library_service_backend.book.model.Book;
import me.vladislav.library_service_backend.library.model.Library;
import me.vladislav.library_service_backend.auth.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BookLoanMapper {

    @Mapping(target = "userId", expression = "java(bookLoan.getUser().getId())")
    @Mapping(target = "bookId", expression = "java(bookLoan.getBook().getId())")
    @Mapping(target = "libraryId", expression = "java(bookLoan.getLibrary().getId())")
    BookLoanDTO toDTO(BookLoan bookLoan);

    @Mapping(target = "user", expression = "java(mapUser(dto.getUserId()))")
    @Mapping(target = "book", expression = "java(mapBook(dto.getBookId()))")
    @Mapping(target = "library", expression = "java(mapLibrary(dto.getLibraryId()))")
    BookLoan toEntity(BookLoanDTO dto);

    default User mapUser(Long id) {
        if (id == null) return null;
        User u = new User();
        u.setId(id);
        return u;
    }

    default Book mapBook(Long id) {
        if (id == null) return null;
        Book b = new Book();
        b.setId(id);
        return b;
    }

    default Library mapLibrary(Long id) {
        if (id == null) return null;
        Library l = new Library();
        l.setId(id);
        return l;
    }
}

