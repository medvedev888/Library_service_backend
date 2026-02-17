package me.vladislav.library_service_backend.common.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class MailService {
    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetLink) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Восстановление пароля");
        msg.setText(
                "Вы запросили восстановление пароля.\n\n" +
                        "Ссылка для сброса (действует ограниченное время):\n" +
                        resetLink + "\n\n" +
                        "Если это были не вы — просто игнорируйте письмо."
        );
        mailSender.send(msg);
    }
}