package per.rabbit.dto;

import com.google.errorprone.annotations.FormatString;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterDTO {
    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, max = 20, message = "password length must be between 6 and 20")
    private String password;
    @Email
    @Size(min = 4, max = 50, message = "email length must be between 4 and 50")
    private String email;
    @Size(min = 6, max = 20, message = "phone length must be between 6 and 20")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "phone number is invalid")
    private String phone;
}
