package kawajava.github.io.security.service;

import kawajava.github.io.mail.EmailSender;
import kawajava.github.io.mail.service.EmailClientService;
import kawajava.github.io.security.model.ChangePassword;
import kawajava.github.io.security.model.EmailObject;
import kawajava.github.io.user.model.User;
import kawajava.github.io.user.repository.UserRepository;
import kawajava.github.io.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LostPasswordServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailClientService emailClientService;

    @Mock
    private UserService userService;

    @InjectMocks
    private LostPasswordService lostPasswordService;

    @Value("${app.serviceAddress}")
    private String serviceAddress;

    @Test
    public void shouldSendLostPasswordLinkWhenEmailExists() {
        var email = "test@example.com";
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");

        var emailObject = new EmailObject(email);

        when(userRepository.findByUsername(email)).thenReturn(Optional.of(user));
        when(emailClientService.getInstance()).thenReturn((EmailSender) emailClientService);

        lostPasswordService.sendLostPasswordLink(emailObject);

        assertThat(user.getHash()).isNotNull();
        assertThat(user.getHashDate()).isNotNull();
    }

    @Test
    public void shouldThrowExceptionWhenEmailDoesNotExist() {
        var email = "notfound@example.com";
        var emailObject = new EmailObject(email);

        when(userRepository.findByUsername(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lostPasswordService.sendLostPasswordLink(emailObject))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Taki email nie istnieje");
    }

    @Test
    public void shouldChangePasswordWhenLinkIsValid() {
        var hash = "validHash";
        var changePassword = new ChangePassword("newPassword", "newPassword", hash);
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setHash(hash);
        user.setHashDate(LocalDateTime.now());

        when(userRepository.findByHash(hash)).thenReturn(Optional.of(user));

        lostPasswordService.changePassword(changePassword);

        assertThat(user.getPassword()).isNotEqualTo("password");
        assertThat(user.getHash()).isNull();
        assertThat(user.getHashDate()).isNull();
    }

    @Test
    public void shouldThrowExceptionWhenPasswordsDoNotMatch() {
        var changePassword = new ChangePassword("password1", "password2", "someHash");
        assertThatThrownBy(() -> lostPasswordService.changePassword(changePassword))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Hasła nie są takie same");
    }

    @Test
    public void shouldThrowExceptionWhenLinkIsInvalid() {
        var changePassword = new ChangePassword("newPassword", "newPassword", "invalidHash");

        when(userRepository.findByHash("invalidHash")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lostPasswordService.changePassword(changePassword))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Nieprawidłowy link");
    }

    @Test
    public void shouldThrowExceptionWhenLinkIsExpired() {
        var hash = "expiredHash";
        var changePassword = new ChangePassword("newPassword", "newPassword", hash);
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setHash(hash);
        user.setHashDate(LocalDateTime.now().minusMinutes(61)); // link wygasł

        when(userRepository.findByHash(hash)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> lostPasswordService.changePassword(changePassword))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Link stracił ważność");
    }
}
