package kawajava.github.io.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class LoginController {

    public LoginController(AuthenticationManager authenticationManager,
                           @Value("${jwt.expirationTime}") long expirationTime,
                           @Value("${jwt.secret}") String secret) {
        this.authenticationManager = authenticationManager;
        this.secret = secret;
        this.expirationTime = expirationTime;
    }

    private final AuthenticationManager authenticationManager;

    private long expirationTime;
    private String secret;

    @PostMapping("/login")
    public String login(@RequestBody LoginCredentials loginCredentials) {
        var authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginCredentials.username(), loginCredentials.password())
        );

        UserDetails principal = (UserDetails) authenticate.getPrincipal();

        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));

        return token;
    }


}
