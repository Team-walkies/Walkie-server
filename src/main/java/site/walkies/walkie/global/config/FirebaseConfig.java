package site.walkies.walkie.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@Slf4j
@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try (InputStream in = new ClassPathResource("firebase/firebase-service-key.json").getInputStream()) {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(in))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("✅ FirebaseApp initialized 성공");
            } else {
                log.info("ℹ️ FirebaseApp이 이미 initialized 되어 있음.");
            }

        } catch (Exception e) {
            log.error("❌ Firebase initialization 실패", e);
            throw new IllegalStateException("Firebase initialization failed", e); // fail-fast
        }
    }
}
