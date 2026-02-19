package me.vladislav.library_service_backend.notification.listener;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.notification.dto.EmailNotificationEvent;
import me.vladislav.library_service_backend.notification.dto.ReservationApprovedEvent;
import me.vladislav.library_service_backend.notification.producer.NotificationProducer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor

@Component
public class ReservationApprovedListener {
    private final NotificationProducer notificationProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ReservationApprovedEvent event) {
        notificationProducer.sendEmail(
                EmailNotificationEvent.builder()
                        .to(event.userEmail())
                        .subject("Бронь подтверждена")
                        .body("Ваша бронь на книгу \"" +
                                event.bookTitle() +
                                "\" подтверждена.")
                        .build()
        );
    }
}
