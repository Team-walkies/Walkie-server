package site.walkies.walkie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication()
@Slf4j
public class WalkieApplication {

	public static void main(String[] args) {
		System.out.println("HealthCheck started");
		SpringApplication.run(WalkieApplication.class, args);
		System.out.println("HealthCheck finished");
		log.info("[Startup] Walkie 서버가 성공적으로 시작되었습니다.");
	}

}
