package kawajava.github.io.user.service;


import kawajava.github.io.user.controller.dto.UserDto;
import kawajava.github.io.user.model.User;
import kawajava.github.io.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(UserDto userDto) {
        validateEmail(userDto.getEmail());
        validateUsername(userDto.getUsername());
        validatePassword(userDto.getPassword());

        var passwordInBcrypt = hashPassword(userDto.getPassword());

        User user = mapUToUser(userDto, passwordInBcrypt,null);
        return userRepository.save(user);
    }

    private void validateEmail(String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
            throw new RuntimeException("Podany email istnieje w bazie danych");
        }
    }

    private void validateUsername(String username) {
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isPresent()) {
            throw new RuntimeException("Ta nazwa użytkownika już istnieje w bazie danych");
        }
    }

    private void validatePassword(String password) {
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            throw new RuntimeException("Hasło musi zawierać dużą literę");
        }
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            throw new RuntimeException("Hasło musi zawierać cyfrę");
        }
        if (!Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
            throw new RuntimeException("Hasło musi zawierać znak specjalny");
        }
    }

    private String hashPassword(String password) {
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    private User mapUToUser(UserDto userDto, String passwordInBcrypt, Long id) {
        return User.builder()
                .id(id)
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .password(passwordInBcrypt)
                .build();
    }
}
