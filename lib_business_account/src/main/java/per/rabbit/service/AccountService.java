package per.rabbit.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import per.rabbit.dao.UserInfoDao;
import per.rabbit.dao.UserInfoMapper;
import per.rabbit.dto.*;
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

    /**
     * 使用 refreshToken 换发新的 accessToken
     *
     * @param refreshToken 登陆时下发的 refreshToken
     * @return 包含新 accessToken 的 UserRefreshVO
     */
    public UserRefreshVO refreshToken(String refreshToken) {
        String userId;
        try {
            Claims claims = authUtil.parseToken(refreshToken);
            // 只允许 refreshToken 换发，防止 accessToken 被当作 refreshToken 使用
            if (!AuthUtil.TYPE_REFRESH.equals(claims.get(AuthUtil.CLAIM_TYPE, String.class))) {
                throw new LoginException("无效的Token!");
            }
            userId = claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new LoginException("登陆已过期，请重新登陆!");
        } catch (LoginException e) {
            throw e;
        } catch (Exception e) {
            throw new LoginException("无效的Token!");
        }

        // 校验通过，重新签发短效 accessToken
        String accessToken = authUtil.generateAccessToken(userId);
        return new UserRefreshVO() {{
            setToken(accessToken);
        }};
    }
}
