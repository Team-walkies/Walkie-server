package site.walkies.walkie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WalkieApplication {

	public static void main(String[] args) {
		System.out.println("HealthCheck started");
		SpringApplication.run(WalkieApplication.class, args);
		System.out.println("HealthCheck finished");
	}

}
