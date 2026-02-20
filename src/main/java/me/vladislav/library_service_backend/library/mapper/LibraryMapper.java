package me.vladislav.library_service_backend.library.mapper;

import me.vladislav.library_service_backend.library.dto.CreateLibraryRequest;
import me.vladislav.library_service_backend.library.dto.LibraryDTO;
import me.vladislav.library_service_backend.library.dto.UpdateLibraryRequest;
import me.vladislav.library_service_backend.library.model.Library;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LibraryMapper {
    @Mapping(target = "id", ignore = true)
    LibraryDTO toDTO(CreateLibraryRequest request);

    @Mapping(target = "id", ignore = true)
    LibraryDTO toDTO(UpdateLibraryRequest request);

    Library toEntity(LibraryDTO dto);

    LibraryDTO toDTO(Library library);
}
