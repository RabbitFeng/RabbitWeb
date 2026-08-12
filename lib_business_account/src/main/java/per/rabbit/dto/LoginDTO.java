package per.rabbit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import per.rabbit.valid.LoginValid;

@Data
@NoArgsConstructor
@LoginValid
public class LoginDTO {
    @Email(message = "email format is incorrect")
    private String email;

    private String phone;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, max = 20, message = "password length must be between 6 and 20")
    private String password;
}
