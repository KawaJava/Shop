package kawajava.github.io.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSimpleService implements EmailSender {

    private final JavaMailSender mailSender;

//    @Async
    @Override
    public void send(String to, String subject, String msg) {
        SimpleMailMessage message = new SimpleMailMessage();
        //message.setFrom("Shop <shop@example.com>");
        //message.setReplyTo("Shop <shop@example.com>");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);
        mailSender.send(message);
    }
}
