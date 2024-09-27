package kawajava.github.io.user.service;

import kawajava.github.io.user.controller.dto.UserDto;
import kawajava.github.io.user.model.User;
import kawajava.github.io.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserCorrectly() {
        var userDto = createUserDto("test@example.com", "testuser", "Password1!");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        ArgumentCaptor<User> userCaptor = forClass(User.class);

        userService.registerUser(userDto);

        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertThat(capturedUser).isNotNull();
        assertThat(capturedUser.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(capturedUser.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(capturedUser.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(capturedUser.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(capturedUser.getPhoneNumber()).isEqualTo(userDto.getPhoneNumber());
        assertThat(capturedUser.getPassword()).isNotNull();
    }

    @Test
    void shouldNotRegisterUserWhenEmailExists() {
        var userDto = createUserDto("test@example.com", "testuser", "Password1!");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.registerUser(userDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Podany email istnieje w bazie danych");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldNotRegisterUserWhenUsernameExists() {
        var userDto = createUserDto("test@example.com", "testuser", "Password1!");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.registerUser(userDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ta nazwa użytkownika już istnieje w bazie danych");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldNotRegisterUserWhenPasswordInvalid() {
        var userDto = createUserDto("test@example.com", "testuser", "password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.registerUser(userDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Hasło musi zawierać dużą literę");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldNotRegisterUserWhenPasswordLacksDigit() {
        var userDto = createUserDto("test@example.com", "testuser", "Password!");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.registerUser(userDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Hasło musi zawierać cyfrę");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldNotRegisterUserWhenPasswordLacksSpecialCharacter() {
        var userDto = createUserDto("test@example.com", "testuser", "Password1");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.registerUser(userDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Hasło musi zawierać znak specjalny");

        verify(userRepository, never()).save(any(User.class));
    }

    private UserDto createUserDto(String email, String username, String password) {
        var userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setUsername(username);
        userDto.setPassword(password);
        return userDto;
    }
}