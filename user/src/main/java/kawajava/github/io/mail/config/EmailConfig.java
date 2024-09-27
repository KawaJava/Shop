package kawajava.github.io.mail.config;

import kawajava.github.io.mail.EmailSender;
import kawajava.github.io.mail.service.EmailSimpleService;
import kawajava.github.io.mail.service.FakeEmailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailConfig {

    @Bean
    @ConditionalOnProperty(name="app.email.sender", matchIfMissing = true, havingValue = "emailSimpleService")
    public EmailSender emailSimpleService(JavaMailSender javaMailSender){
        return new EmailSimpleService(javaMailSender);
    }

    @Bean
    @ConditionalOnProperty(name="app.email.sender", havingValue = "fakeEmailService")
    public EmailSender fakeEmailService(){
        return new FakeEmailService();
    }
}
