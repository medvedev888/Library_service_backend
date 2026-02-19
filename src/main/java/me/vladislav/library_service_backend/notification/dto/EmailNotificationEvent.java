package me.vladislav.library_service_backend.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailNotificationEvent implements Serializable {
    private Long userId;
    private String to;
    private String subject;
    private String body;
}
