package per.rabbit.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class AuthUtil {
    private final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final String SECRET = "rabbit_web_login";

    @Value("#{${auth.expire.access} * 1000}")
    public long ACCESS_TOKEN_EXP;

    @Value("#{${auth.expire.refresh} * 1000}")
    public long REFRESH_TOKEN_EXP;

    public final String generateAccessToken(String userId) {
        return generateToken(userId, ACCESS_TOKEN_EXP);
    }

    public final String generateRefreshToken(String userId) {
        return generateToken(userId, REFRESH_TOKEN_EXP);
    }

    public final String generateToken(String userId, long expire) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(KEY)
                .compact();
    }

    public Claims parseToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String getUserId(String token) {
        try {
            return parseToken(token).getSubject();
        } catch (ExpiredJwtException e) {
            // 过期
        } catch (UnsupportedJwtException e) {
            // 不支持
        } catch (MalformedJwtException e) {
            // 缩形
        } catch (SignatureException e) {
            // 签名错误
        } catch (IllegalArgumentException e) {
            // 为空
        }
        return "";
    }
}
