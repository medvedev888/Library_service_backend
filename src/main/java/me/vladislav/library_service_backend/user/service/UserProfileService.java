package me.vladislav.library_service_backend.user.service;


import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.exception.UserNotFoundException;
import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.auth.repository.UserRepository;
import me.vladislav.library_service_backend.user.dto.UserProfileDTO;
import me.vladislav.library_service_backend.user.mapper.UserProfileMapper;
import me.vladislav.library_service_backend.user.model.UserProfile;
import me.vladislav.library_service_backend.user.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;


    @Transactional
    public UserProfile create(UserProfileDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));

        UserProfile userProfile = userProfileMapper.toEntity(dto);
        userProfile.setUser(user);

        return userProfileRepository.save(userProfile);
    }

}
