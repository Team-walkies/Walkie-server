package site.walkies.walkie.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import site.walkies.walkie.domain.auth.service.dto.response.KakaoUserInfoResponseDto;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final WebClient webClient;

    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        return webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();
    }
}
