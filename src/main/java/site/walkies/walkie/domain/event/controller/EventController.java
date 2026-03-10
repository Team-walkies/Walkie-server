package site.walkies.walkie.domain.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.walkies.walkie.domain.event.service.DailyEggEventService;
import site.walkies.walkie.domain.event.service.dto.DailyEggEventResponse;
import site.walkies.walkie.global.auth.dto.MemberPrincipal;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final DailyEggEventService dailyEggEventService;

    @GetMapping("/daily-egg")
    public SuccessResponse<DailyEggEventResponse> getDailyEgg(@AuthenticationPrincipal MemberPrincipal principal) {
        // return SuccessResponse.ok(
        //     dailyEggEventService.provideDailyEgg(principal.getMemberId())
        // );
        throw new CustomException(ErrorCode.EVENT_PERIOD_ENDED);
    }
}
