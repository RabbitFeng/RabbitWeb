package per.rabbit.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("user_info")
public class UserInfoDao {
    @TableField("id")
    private Long id;
    @TableField("user_id")
    private String userId;
    @TableField("user_name")
    private String userName;
    @TableField("pwd")
    private String pwd;
    @TableField("email")
    private String email;
    @TableField("phone")
    private String phone;
    @TableField("created_time")
    private long createdTime;
    @TableField("updated_time")
    private long updateTime;
}
