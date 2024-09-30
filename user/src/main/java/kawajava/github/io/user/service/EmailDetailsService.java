package kawajava.github.io.user.service;

import kawajava.github.io.mail.service.EmailClientService;
import kawajava.github.io.user.model.User;
import kawajava.github.io.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailDetailsService {

    private final UserRepository userRepository;
    private final EmailClientService emailClientService;
    @Value("${app.serviceAddress}")
    private String serviceAddress;

    public void sendActivationLink(User user) {
        String token = generateActivationToken(user);
        user.setActivationToken(token);
        user.setActivationTokenDate(LocalDateTime.now());
        userRepository.save(user);

        String link = createActivationLink(token);
        emailClientService.getInstance()
                .send(user.getEmail(), "Aktywuj swoje konto", createActivationMessage(link));
    }

    public String generateActivationToken(User user) {
        String toHash = user.getId() + user.getUsername() + LocalDateTime.now();
        return DigestUtils.sha256Hex(toHash);
    }

    public String createActivationLink(String token) {
        return serviceAddress + "/activate/" + token;
    }

    public String createActivationMessage(String link) {
        return "Witaj!\n\nKliknij poniższy link, aby aktywować swoje konto:\n" + link + "\n\nDziękujemy!";
    }
}
