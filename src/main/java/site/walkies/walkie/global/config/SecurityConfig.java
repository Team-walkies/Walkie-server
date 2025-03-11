package site.walkies.walkie.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.walkies.walkie.domain.member.service.MemberLoginService;
import site.walkies.walkie.global.auth.filter.JwtFilter;
import site.walkies.walkie.global.auth.utils.JWTProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTProvider jwtProvider;
    private final MemberLoginService memberLoginService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/auth/login","/swagger-ui.html", "/v3/api-docs/**","/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable()) // ✅ 로그인 폼 비활성화 (SNS 로그인만 사용)
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable()) // ✅ HTTP Basic 인증 비활성화
                .addFilterBefore(new JwtFilter(jwtProvider, memberLoginService), UsernamePasswordAuthenticationFilter.class); // ✅ JwtFilter 직접 생성하여 추가

        return http.build();
    }
}
