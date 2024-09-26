package kawajava.github.io.user.service;


import kawajava.github.io.user.model.User;
import kawajava.github.io.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }
}
