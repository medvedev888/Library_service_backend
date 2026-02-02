package me.vladislav.library_service_backend.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.vladislav.library_service_backend.library.model.LibraryStatus;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibraryDTO {
    private Long id;
    private Map<String, Object> address;
    private Long staffNumber;
    private LibraryStatus status;
    private Set<Long> bookIds;
}
