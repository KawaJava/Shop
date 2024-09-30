package kawajava.github.io.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private boolean isActive;
    private String activationToken;
    private LocalDateTime activationTokenDate;
    private String hash;
    private LocalDateTime HashDate;
}
