package me.vladislav.library_service_backend.user.mapper;

import me.vladislav.library_service_backend.user.dto.LibrarianDTO;
import me.vladislav.library_service_backend.user.model.Librarian;
import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.library.model.Library;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface LibrarianMapper {

    @Mapping(target = "userId", expression = "java(librarian.getUser().getId())")
    @Mapping(target = "libraryId", expression = "java(librarian.getLibrary().getId())")
    LibrarianDTO toDto(Librarian librarian);

    @Mapping(target = "user", expression = "java(mapUser(dto.getUserId()))")
    @Mapping(target = "library", expression = "java(mapLibrary(dto.getLibraryId()))")
    Librarian toEntity(LibrarianDTO dto);

    default User mapUser(Long id) {
        if (id == null) return null;
        User u = new User();
        u.setId(id);
        return u;
    }

    default Library mapLibrary(Long id) {
        if (id == null) return null;
        Library l = new Library();
        l.setId(id);
        return l;
    }

}
