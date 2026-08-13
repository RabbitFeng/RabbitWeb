package per.rabbit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.test.util.ReflectionTestUtils;
import per.rabbit.util.AuthUtil;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * 批量生成测试用 Token 的小工具。
 * 不依赖数据库/完整 Spring 上下文，手动构造 AuthUtil 并注入配置，复用真实签发逻辑（含 type claim）。
 * 运行：./mvnw -pl app test -Dtest=per.rabbit.util.TokenGenTest
 */
public class TokenGenTest {

    // 与 application-user.yaml 保持一致
    private static final String SECRET = "rabbit_web_login_aaaaaaaaaaaaaaa";
    private static final long ACCESS_EXP_MS = 600L * 1000;        // 10 分钟
    private static final long REFRESH_EXP_MS = 864000L * 1000;    // 10 天

    private static AuthUtil authUtil;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static void buildAuthUtil() {
        authUtil = new AuthUtil();
        ReflectionTestUtils.setField(authUtil, "SECRET", SECRET);
        ReflectionTestUtils.setField(authUtil, "ACCESS_TOKEN_EXP", ACCESS_EXP_MS);
        ReflectionTestUtils.setField(authUtil, "REFRESH_TOKEN_EXP", REFRESH_EXP_MS);
    }

    @BeforeAll
    public static void setup() {
        buildAuthUtil();
    }

    private static final long CURRENT = 0;
    private static final long ONE_MINUTE = 60 * 1000;
    private static final long ONE_HOUR = 60 * ONE_MINUTE;
    private static final long ONE_DAY = 24 * ONE_HOUR;
    private static final long ONE_WEEK = 7 * ONE_DAY;
    private static final long ONE_YEAR = 365 * ONE_DAY;


    @Test
    public void generateBatch() {
        // 想造多少个用户就改这里
        String[] userIds = {"dongzhongfeng"};

        for (String userId : userIds) {
            System.out.println("AccessToken");
            printToken(userId, AuthUtil.TYPE_ACCESS, CURRENT, ONE_MINUTE, ONE_DAY, ONE_WEEK, ONE_YEAR);
            System.out.println("RefreshToken");
            printToken(userId, AuthUtil.TYPE_REFRESH, CURRENT, ONE_MINUTE, ONE_DAY, ONE_WEEK, ONE_YEAR);
        }
    }

    private static void printToken(String userId, String type, long... accessExps) {
        System.out.println("```");
        for (long accessExp : accessExps) {
            String token = authUtil.generateToken(userId, type, accessExp);
            System.out.println("Expire: " + sdf.format(System.currentTimeMillis() + accessExp) + "\n" + token);
        }
        System.out.println("```");
    }
}
