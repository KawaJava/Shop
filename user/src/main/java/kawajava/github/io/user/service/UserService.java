package kawajava.github.io.user.service;


import jakarta.transaction.Transactional;
import kawajava.github.io.exception.ResourceNotFoundException;
import kawajava.github.io.user.controller.dto.UserDto;
import kawajava.github.io.user.model.User;
import kawajava.github.io.user.model.UserRole;
import kawajava.github.io.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailDetailsService emailDetailsService;
    @Value("${app.serviceAddress}")
    private String serviceAddress;

    @Transactional
    public User registerUser(UserDto userDto) {
        validateEmail(userDto.getEmail());
        validateUsername(userDto.getUsername());
        validatePassword(userDto.getPassword());
        var passwordInBcrypt = hashPassword(userDto.getPassword());

        var user = mapToUser(userDto, passwordInBcrypt,null, false);

        emailDetailsService.sendActivationLink(user);
        return userRepository.save(user);
    }

    public User findByActivationToken(String token) {
        return userRepository.findByActivationToken(token).orElseThrow(() -> new ResourceNotFoundException(token));
    }

    public void activateUser(Long id) {
        userRepository.activateUser(id);
    }

    public void deleteUser(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(userDetails.getUsername()));

        userRepository.delete(user);
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

    public void validatePassword(String password) {
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

    private User mapToUser(UserDto userDto, String passwordInBcrypt, Long id, boolean isActive) {
        return User.builder()
                .id(id)
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .password(passwordInBcrypt)
                .isActive(isActive)
                .role(UserRole.USER)
                .build();
    }

    public String updateUser(UserDetails userDetails, UserDto userDto) {
        Optional<User> userOptional = userRepository.findByUsername(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException(userOptional.get().getUsername());
        }
        var user = userOptional.get();
        validateEmail(userDto.getEmail());
        validateUsername(userDto.getUsername());
        validatePassword(userDto.getPassword());
        var passwordInBcrypt = hashPassword(userDto.getPassword());
        var updatedUser = mapToUser(userDto, passwordInBcrypt, user.getId(), true);
        userRepository.save(updatedUser);
        return "Dane użytkownika zostały zaktualizowane";
    }
}
