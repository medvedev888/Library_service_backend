package me.vladislav.library_service_backend.notification.listener;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.common.mail.MailService;
import me.vladislav.library_service_backend.notification.config.RabbitConfiguration;
import me.vladislav.library_service_backend.notification.dto.EmailNotificationEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor

@Component
public class EmailNotificationListener {
    private final MailService mailService;

    @RabbitListener(queues = RabbitConfiguration.EMAIL_QUEUE)
    public void handleEmailNotification(EmailNotificationEvent event) {
        mailService.sendGenericEmail(
                event.getTo(),
                event.getSubject(),
                event.getBody()
        );
    }

}
