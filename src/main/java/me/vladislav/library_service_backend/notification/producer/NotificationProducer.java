package me.vladislav.library_service_backend.notification.producer;


import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.notification.config.RabbitConfiguration;
import me.vladislav.library_service_backend.notification.dto.EmailNotificationEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class NotificationProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendEmail(EmailNotificationEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfiguration.EXCHANGE,
                RabbitConfiguration.EMAIL_ROUTING_KEY,
                event
        );
    }

}
