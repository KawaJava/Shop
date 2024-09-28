package kawajava.github.io.security.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class ChangePassword {
    @NotBlank
    @Length(min = 8, max = 20)
    private String password;
    private String repeatPassword;
    private String hash;
}
