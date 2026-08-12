package per.rabbit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserDataVO {
    private String name; // 姓名
    private int gender; // 性别
    private float height; // 身高
    private float weight; // 体重
}
