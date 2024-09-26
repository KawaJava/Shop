package kawajava.github.io.user.controller.dto;

import jakarta.validation.Valid;
import kawajava.github.io.user.model.User;
import kawajava.github.io.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/registry")
    public User createUser(@RequestBody @Valid UserDto userDto) {
        return userService.registerUser(userDto);
    }

    @GetMapping
    public String hello() {
        return "Hello World from UserController";
    }
}
