package kawajava.github.io.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import kawajava.github.io.security.model.LoginCredentials;
import kawajava.github.io.security.model.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final long expirationTime;
    private final String secret;

    public LoginController(AuthenticationManager authenticationManager,
                           @Value("${jwt.expirationTime}") long expirationTime,
                           @Value("${jwt.secret}") String secret) {
        this.authenticationManager = authenticationManager;
        this.secret = secret;
        this.expirationTime = expirationTime;
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginCredentials loginCredentials) {
        String usernameOrEmail = loginCredentials.usernameOrEmail();
        String loginType = usernameOrEmail.contains("@") ? "email" : "username";

        var authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameOrEmail, loginCredentials.password())
        );

        var principal = (UserDetails) authenticate.getPrincipal();

        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));

        return new Token(token);
    }
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
