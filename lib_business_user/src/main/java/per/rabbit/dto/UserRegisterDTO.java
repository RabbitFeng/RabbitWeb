package per.rabbit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterDTO {
    @NotBlank(message = "username cannot be blank")
    @Size(min = 4, max = 20, message = "username length must be between 4 and 20")
    private String username;
    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, max = 20, message = "password length must be between 6 and 20")
    private String password;
    @Size(min = 4, max = 50, message = "email length must be between 4 and 50")
    @Email
    private String email;
    @Size(min = 6, max = 20, message = "phone length must be between 6 and 20")
    private String phone;
}
