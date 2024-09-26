package kawajava.github.io.user.service;


import kawajava.github.io.user.controller.dto.UserDto;
import kawajava.github.io.user.model.User;
import kawajava.github.io.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(UserDto userDto) {

        Optional<User> userByEmail = userRepository.findByEmail(userDto.getEmail());
        if(userByEmail.isPresent()) throw new RuntimeException();
        Optional<User> userByUsername = userRepository.findByUsername(userDto.getUsername());
        if(userByUsername.isPresent()) throw new RuntimeException();

        User user = User.builder()
                .id(null)
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
        return userRepository.save(user);
    }
}
