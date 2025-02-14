package site.walkies.walkie.healthCheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("api/v1/healthCheck")
    public String healthCheck() {
        return "OK";
    }
}
