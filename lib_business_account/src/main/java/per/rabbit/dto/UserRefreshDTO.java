package per.rabbit.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRefreshDTO {
    @NotEmpty(message = "token不能为空")
    private String token;
}
