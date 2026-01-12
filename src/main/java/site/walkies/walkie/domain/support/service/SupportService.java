package site.walkies.walkie.domain.support.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.walkies.walkie.global.webhook.DiscordNotifier;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupportService {
    private final DiscordNotifier discordNotifier;

    // discord webhook 전송
    public void send(String message, String information, Long memberId) {
        // 디스코드 전송
        String OpinionMessage = "**의견이 들어왔습니다.**\n"
                + "- 사용자 ID: " + memberId + "\n"
                + "- 기기 및 추가 정보: " + information + "\n"
                + "- 의견: " + message + "\n";

        discordNotifier.sendOpinionMessage(OpinionMessage);
    }
}
