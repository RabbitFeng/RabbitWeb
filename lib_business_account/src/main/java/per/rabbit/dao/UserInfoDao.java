package per.rabbit.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@TableName("account_info")
public class UserInfoDao {
    @TableField("id")
    private Long id;
    @TableField("account_id")
    private String accountId;
    @TableField("pwd")
    private String pwd;
    @TableField("email")
    private String email;
    @TableField("phone")
    private String phone;
    @TableField("created_time")
    private LocalDateTime createdTime;
    @TableField("updated_time")
    private LocalDateTime updateTime;
}
