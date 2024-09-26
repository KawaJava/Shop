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

        Optional<User> userByEmail = userRepository.findByEmail(userDto.getEmail());
        if(userByEmail.isPresent()) throw new RuntimeException("Podany email istnieje w bazie danych");
        Optional<User> userByUsername = userRepository.findByUsername(userDto.getUsername());
        if(userByUsername.isPresent()) throw new RuntimeException("Ta nazwa użytkownika już istnieje w bazie danych");

        var password = userDto.getPassword();

        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            throw new RuntimeException("Hasło musi zawierać dużą literę");
        }
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            throw new RuntimeException("Hasło musi zawierać cyfrę");
        }
        if (!Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
            throw new RuntimeException("Hasło musi zawierać znak specjalny");
        }

        var passwordEncoder = new BCryptPasswordEncoder();
        var passwordInBcrypt = passwordEncoder.encode(password);

        User user = User.builder()
                .id(null)
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .password(passwordInBcrypt)
                .build();
        return userRepository.save(user);
    }
}
