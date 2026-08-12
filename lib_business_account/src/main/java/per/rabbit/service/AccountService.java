package per.rabbit.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import per.rabbit.dao.UserInfoDao;
import per.rabbit.dao.UserInfoMapper;
import per.rabbit.dto.LoginDTO;
import per.rabbit.dto.LoginVO;
import per.rabbit.dto.UserRegisterDTO;
import per.rabbit.dto.UserRegisterVO;
import per.rabbit.exc.LoginException;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserRegisterVO register(UserRegisterDTO userRegisterDTO) throws AccountException {
        String email = userRegisterDTO.getEmail();
        String phone = userRegisterDTO.getPhone();
        boolean hasEmail = email != null && !email.isEmpty();
        boolean hasPhone = phone != null && !phone.isEmpty();
        // 2. 检查用户是否存在
        UserInfoDao userInfoDao = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfoDao>()
                .select(UserInfoDao::getAccountId)
                .eq(hasEmail, UserInfoDao::getEmail, userRegisterDTO.getEmail())
                .or(hasEmail && hasPhone)
                .eq(hasPhone, UserInfoDao::getPhone, userRegisterDTO.getPhone())
        );
        if (userInfoDao != null) {
            throw new AccountException("用户已存在!");
        }

        userInfoMapper.insert(new UserInfoDao() {{
            setAccountId(UUID.randomUUID().toString().replace("-", ""));
            setPwd(passwordEncoder.encode(userRegisterDTO.getPassword()));
            setEmail(userRegisterDTO.getEmail());
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
    @Transactional(rollbackFor = Exception.class)
    public LoginVO login(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String phone = loginDTO.getPhone();
        String password = loginDTO.getPassword();

        // 按 email 或 phone 查询（至少有一个，已由 LoginValidator 保证）
        boolean hasEmail = email != null && !email.isBlank();
        boolean hasPhone = phone != null && !phone.isBlank();
        UserInfoDao userInfoDao = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfoDao>()
                .select(UserInfoDao::getAccountId, UserInfoDao::getPwd)
                .eq(hasEmail, UserInfoDao::getEmail, email)
                .or(hasEmail && hasPhone)
                .eq(hasPhone, UserInfoDao::getPhone, phone));

        if (userInfoDao == null || !passwordEncoder.matches(password, userInfoDao.getPwd())) {
            throw new LoginException("用户名或密码错误!");
        }

        String userId = userInfoDao.getAccountId();

        // 默认成功
        // 2. 下发Token, 下发两个Token，AccessToken用于接口鉴权，时间较短；loginToken用于刷新AccessToken，也可以作为登陆用户
        String accessToken = authUtil.generateAccessToken(userId);
        String refreshToken = authUtil.generateRefreshToken(userId);
        return new LoginVO() {{
            setAccessToken(accessToken);
            setRefreshToken(refreshToken);
        }};
    }
}
