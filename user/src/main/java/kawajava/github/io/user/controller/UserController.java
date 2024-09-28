package kawajava.github.io.user.controller;

import jakarta.validation.Valid;
import kawajava.github.io.user.controller.dto.UserDto;
import kawajava.github.io.user.model.User;
import kawajava.github.io.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/registry")
    public User createUser(@RequestBody @Valid UserDto userDto) {
        return userService.registerUser(userDto);
    }

    @PutMapping("/user/edit")
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid UserDto userDto) {
        String response = userService.updateUser(userDetails, userDto);
        return ResponseEntity.ok(response);
    }

}
