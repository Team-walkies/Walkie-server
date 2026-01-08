package site.walkies.walkie.domain.webhook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.global.webhook.DiscordNotifier;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebhookService {
    private final DiscordNotifier discordNotifier;

    // discord webhook 전송
    public void send(String message, String information, Long memberId) {
        // 디스코드 전송
        String OpinionMessage = "**의견이 들어왔습니다.**\n"
                + "- 사용자 닉네임: " + userName + "\n"
                + "- 기기 및 추가 정보: " + information + "\n"
                + "- 의견: " + message + "\n";

        discordNotifier.sendOpinionMessage(OpinionMessage);
    }
}
