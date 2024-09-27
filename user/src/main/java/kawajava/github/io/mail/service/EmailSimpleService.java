package kawajava.github.io.mail.service;

import kawajava.github.io.mail.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSimpleService implements EmailSender {

    private final JavaMailSender mailSender;

    @Value("$email.for.sending.password.asking")
    private String email;

    @Async
    @Override
    public void send(String to, String subject, String msg) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Shop >"+ email + ">");
        message.setReplyTo("Shop >"+ email + ">");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);
        mailSender.send(message);
    }
}
