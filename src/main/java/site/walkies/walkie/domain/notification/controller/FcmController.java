package site.walkies.walkie.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.notification.service.FcmService;
import site.walkies.walkie.domain.notification.service.dto.request.FcmSendRequestDto;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FcmController {

    private final FcmService fcmService;

    @Operation(
            summary = "FCM 테스트 메시지 전송",
            description = "Firebase Cloud Messaging(FCM)을 통해 알림 메시지를 디바이스에 전송하는 테스트 API입니다." +
                "token에 **안드로이드 또는 iOS에서 발급받은 디바이스 토큰을 입력합니다.**, 해당 기기로 테스트용 알림을 전송합니다."
            )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "FCM 테스트 메시지 전송 완료"),
            @ApiResponse(responseCode = "500", description = "FCM 메시지 전송에 실패했습니다.")
    })
    @PostMapping(value="/test", consumes= MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse<String> sendTestNotification(@Valid @RequestBody FcmSendRequestDto requestDto) {
        fcmService.sendNotification(requestDto);
        return SuccessResponse.ok("FCM 테스트 메시지 전송 완료");
    }
}