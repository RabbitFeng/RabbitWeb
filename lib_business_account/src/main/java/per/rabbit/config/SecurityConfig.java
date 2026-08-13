package per.rabbit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private RestAuthEntryPoint restAuthEntryPoint;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        log.info("JwtConfig configure: ");
        return http.csrf(AbstractHttpConfigurer::disable) // 无状态JWT不需要CSRF
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(ahr ->
//                        ahr.anyRequest().permitAll()) // TODO: 测试用，全部不加登录鉴权
                        ahr.requestMatchers("api/account/login").permitAll()
                                .requestMatchers("api/account/register").permitAll()
                                .requestMatchers("api/account/refresh").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(httpRequest ->
                        httpRequest.authenticationEntryPoint(restAuthEntryPoint))
                // JWT过滤器插到用户名密码过滤器前
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
