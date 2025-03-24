package site.walkies.walkie.healthCheck;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Operation(
            summary = "초기 연결 테스트용 API",
            description = "이걸 발견한 당신에게... 드래곤 다이노가 오늘 당신의 디버깅을 도와줄 거예요 \uD83E\uDD95✨"
    )
    @GetMapping("api/v1/healthCheck")
    public String healthCheck() {
        return "OK";
    }
}
