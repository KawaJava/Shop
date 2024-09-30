package kawajava.github.io.jwtblacklist.controller;

import kawajava.github.io.jwtblacklist.service.JWTBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JWTBlacklistController {

    private JWTBlacklistService jwtBlacklistService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        var jwt = token.substring(7);  // Usunięcie "Bearer " z tokenu
        jwtBlacklistService.invalidateToken(jwt);
        return ResponseEntity.ok("Zostałeś wylogowany.");
    }
}

