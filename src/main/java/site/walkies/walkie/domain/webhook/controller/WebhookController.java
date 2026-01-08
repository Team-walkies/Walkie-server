package site.walkies.walkie.domain.webhook.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.walkies.walkie.domain.egg.service.dto.response.EggResponse;
import site.walkies.walkie.domain.webhook.service.WebhookService;
import site.walkies.walkie.domain.webhook.service.dto.WebhookRequestDto;
import site.walkies.walkie.global.auth.dto.MemberPrincipal;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @Operation(
            summary = "의견 전달 웹훅",
            description = "디스코드 웹훅을 통해서 의견을 알림으로 보냅니다. (detail : 내용, information : 기기 및 부가 정보)"
    )
    @PostMapping("/opinion")
    public SuccessResponse<?> sendOpinion(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody WebhookRequestDto webhookRequestDto) {
        webhookService.send(webhookRequestDto.getDetail(), webhookRequestDto.getInformation(), memberPrincipal.getMemberId());
        return SuccessResponse.ok();
    }
}
