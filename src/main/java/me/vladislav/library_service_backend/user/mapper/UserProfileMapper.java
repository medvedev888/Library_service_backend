package me.vladislav.library_service_backend.user.mapper;


import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.user.dto.UserProfileDTO;
import me.vladislav.library_service_backend.user.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "userId", expression = "java(profile.getUser().getId())")
    UserProfileDTO toDto(UserProfile profile);

    @Mapping(target = "user", expression = "java(mapUser(dto.getUserId()))")
    @Mapping(target = "age", ignore = true)
    UserProfile toEntity(UserProfileDTO dto);

    default User mapUser(Long id) {
        if (id == null) return null;
        User u = new User();
        u.setId(id);
        return u;
    }

}

