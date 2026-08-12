package per.rabbit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginVO {
    private String accessToken;
    private String refreshToken;
}
