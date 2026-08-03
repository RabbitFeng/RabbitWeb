package per.rabbit.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.rabbit.dao.UserInfoDao;
import per.rabbit.dao.UserInfoMapper;
import per.rabbit.dto.LoginDTO;
import per.rabbit.dto.LoginVO;
import per.rabbit.dto.UserRegisterDTO;
import per.rabbit.dto.UserRegisterVO;
import per.rabbit.util.AuthUtil;

import javax.security.auth.login.AccountException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j(topic = "AccountService")
@Service
public class AccountService {
    /**
     * 登陆用户map
     */
    private final ConcurrentHashMap<String, String> loginUserMap = new ConcurrentHashMap<>();

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private UserInfoMapper userInfoMapper;

    public UserRegisterVO register(UserRegisterDTO userRegisterDTO) throws AccountException {
        // 2. 检查用户是否存在
        UserInfoDao userInfoDao = userInfoMapper.selectOne(new QueryWrapper<UserInfoDao>()
                .select("user_id")
                .eq("email", userRegisterDTO.getUsername())
                .or()
                .eq("phone", userRegisterDTO.getPhone())
        );
        if (userInfoDao.getUserId() != null) {
            throw new AccountException("用户已存在");
        }

        userInfoMapper.insert(new UserInfoDao() {{
            setUserId(UUID.randomUUID().toString().replace("-", ""));
            setUserName(userRegisterDTO.getUsername());
            setPwd(userRegisterDTO.getPassword());
            setEmail(userRegisterDTO.getUsername());
            setPhone(userRegisterDTO.getPhone());
        }});

        return new UserRegisterVO();
    }

    /**
     * 用户登陆，username 支持邮箱，电话
     *
     * @param loginDTO
     * @return
     */
    public LoginVO login(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String phone = loginDTO.getPhone();
        String password = loginDTO.getPassword();
        // 1. 验证登陆成功、失败
        UserInfoDao userInfoDao = userInfoMapper.selectOne(new QueryWrapper<>() {{
            select("user_id");
            eq("username", phone);
            eq("password", password);
        }});

        String userId = userInfoDao.getUserId();

        // 默认成功
        // 2. 下发Token, 下发两个Token，AccessToken用于接口鉴权，时间较短；loginToken用于刷新AccessToken，也可以作为登陆用户
        String accessToken = authUtil.generateAccessToken(userId);
        String refreshToken = authUtil.generateRefreshToken(userId);
        return new LoginVO() {{
            setAccessToken(accessToken);
            setRefreshToken(refreshToken);
        }};
    }

    /**
     * 获取用户ID
     *
     * @param token
     * @return
     */
    public String getUserId(String token) {
        return authUtil.getUserId(token);
    }

    public boolean logout(String token) {
        loginUserMap.remove(authUtil.getUserId(token));
        return true;
    }
}
