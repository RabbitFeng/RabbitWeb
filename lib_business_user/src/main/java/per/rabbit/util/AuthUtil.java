package per.rabbit.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class AuthUtil {

    @Value("${auth.secret}")
    private String SECRET;

    @Value("#{${auth.expire.access} * 1000}")
    private long ACCESS_TOKEN_EXP;

    @Value("#{${auth.expire.refresh} * 1000}")
    private long REFRESH_TOKEN_EXP;

    private Key cachedKey = null;

    private Key getKey(){
        if(cachedKey == null){
            cachedKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        }
        return cachedKey;
    }


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
                .signWith(getKey())
                .compact();
    }

    public Claims parseToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
