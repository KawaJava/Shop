package kawajava.github.io.security.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class ChangePassword {
    @NotBlank
    @Length(min = 8, max = 20)
    private String password;
    private String repeatPassword;
    private String hash;
}
