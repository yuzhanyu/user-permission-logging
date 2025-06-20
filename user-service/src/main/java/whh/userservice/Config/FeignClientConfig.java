package whh.userservice.Config;
import feign.RequestInterceptor;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Date;

/**
 * @author hanghang
 * @date 2025/6/19
 * @Description
 */
@Configuration

public class FeignClientConfig {
    // 服务间共享的密钥（从配置中心获取）
    private final String SECRET = "your-256-bit-secret-key-here-1234567890ABCD";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = generateServiceToken();
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }

    private String generateServiceToken() {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

        return Jwts.builder()
                .setSubject("userservice")  // 服务标识
                .setIssuer("internal-auth")
                .setExpiration(new Date(System.currentTimeMillis() + 600000)) // 10分钟有效期
                .signWith(key)
                .compact();
    }
}