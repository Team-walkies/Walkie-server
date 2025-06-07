package site.walkies.walkie.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.walkies.walkie.global.logging.RequestLoggingInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
@RequiredArgsConstructor
public class WebmvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH","DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3000);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLoggingInterceptor())
                .addPathPatterns("/**"); // 모든 요청 로그 수집
    }
}
