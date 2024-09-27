package kawajava.github.io.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String login(@RequestBody LoginCredentials loginCredentials) {
        var authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginCredentials.username(), loginCredentials.password())
        );

        UserDetails principal = (UserDetails) authenticate.getPrincipal();
        return String.valueOf(authenticate.isAuthenticated());
    }


}
