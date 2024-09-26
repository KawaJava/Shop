package kawajava.github.io.user.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserDto {
    @NotBlank
    @Length(min = 3, max = 255)
    private String firstName;
    @NotBlank
    @Length(min = 3, max = 255)
    private String lastName;
    @NotBlank
    @Length(min = 3, max = 255)
    private String username;
    @NotNull
    @Email
    private String email;
    @NotBlank
    @Length(min = 9, max = 20)
    private String phoneNumber;
    @NotBlank
    @Length(min = 8, max = 20)
    private String password;
}
