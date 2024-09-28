package kawajava.github.io.security.service;

import jakarta.transaction.Transactional;
import kawajava.github.io.mail.service.EmailClientService;
import kawajava.github.io.security.model.ChangePassword;
import kawajava.github.io.security.model.EmailObject;
import kawajava.github.io.user.model.User;
import kawajava.github.io.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LostPasswordService {

    private final UserRepository userRepository;
    private final EmailClientService emailClientService;
    @Value("${app.serviceAddress}")
    private String serviceAddress;

    @Transactional
    public void sendLostPasswordLink(EmailObject email) {
        var user = userRepository.findByUsername(email.getEmail())
                .orElseThrow(() -> new RuntimeException("Taki email nie istnieje"));

        String hash = generateHashForLostPassword(user);
        user.setHash(hash);
        user.setHashDate(LocalDateTime.now());
        emailClientService.getInstance()
                .send(email.getEmail(), "Zresetuj hasło", createMessage(createLink(hash)));
    }

    private String createMessage(String hashLink) {
        return "Wygenerowaliśmy dla Ciebie link do zmiany hasła" +
                "\n\nKliknij link, żeby zresetować hasło: " +
                "\n" + hashLink +
                "\n\nDziękujemy.";
    }

    private String createLink(String hash) {
        return serviceAddress + "/lostPassword/" + hash;
    }

    private String generateHashForLostPassword(User user) {
        String toHash = user.getId() + user.getUsername() + user.getPassword() + LocalDateTime.now();
        return DigestUtils.sha256Hex(toHash);
    }

    @Transactional
    public void changePassword(ChangePassword changePassword){
        if(!Objects.equals(changePassword.getPassword(), changePassword.getRepeatPassword())) {
            throw new RuntimeException("Hasła nie są takie same");
        }
        var user = userRepository.findByHash(changePassword.getHash())
                .orElseThrow(() -> new RuntimeException("Nieprawidłowy link"));
        if(user.getHashDate().plusMinutes(60).isAfter(LocalDateTime.now())){
            user.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(changePassword.getPassword()));
            user.setHash(null);
            user.setHashDate(null);
        } else {
            throw new RuntimeException("Link stracił ważność");
        }

    }

}
