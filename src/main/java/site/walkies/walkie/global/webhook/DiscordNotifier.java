package site.walkies.walkie.global.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

// 디스코드 웹훅
@Component
public class DiscordNotifier {

    private final WebClient webClient;


    public DiscordNotifier(WebClient.Builder builder,
                           @Value("${DISCORD_HOOK}") String webhookUrl) {
        this.webClient = builder.baseUrl(webhookUrl).build();
    }

    public void sendMessage(String content) {
        webClient.post()
                .bodyValue(new DiscordMessage(content))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
    }

    private record DiscordMessage(String content) {}
}
