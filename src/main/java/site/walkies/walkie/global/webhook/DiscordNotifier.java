package site.walkies.walkie.global.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

// 디스코드 웹훅
@Component
public class DiscordNotifier {

    @Value("${DISCORD_HOOK_ERROR}")
    private String errorHook;

    @Value("${DISCORD_HOOK_REGIST}")
    private String registHook;

    private final WebClient.Builder webClient;


    public DiscordNotifier(WebClient.Builder builder) {
        this.webClient = builder;
    }
    
    private void sendMessage(String content, String webhookUrl) {
        webClient.baseUrl(webhookUrl).
                build()
                .post()
                .bodyValue(new DiscordMessage(content))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
    }
    
    // 에러 메시지 웹훅
    public void sendErrorMessage(String content) {
        sendMessage(content, errorHook);
    }

    // 회원가입 웹훅
    public void sendRegistMessage(String content) {
        sendMessage(content, registHook);
    }

    private record DiscordMessage(String content) {}
}
