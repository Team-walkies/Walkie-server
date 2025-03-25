package site.walkies.walkie.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI walkieOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🚶 Walkie API Docs")
                        .description("Walkie 프로젝트의 백엔드 API 문서입니다.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Walkie Backend Dev Team")
                                .url("https://github.com/Team-walkies/Walkie-server")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))// 여기서 인증 "적용"함
                .addServersItem(new Server().description("기본 URL").url("/api/v1"));
    }
}


