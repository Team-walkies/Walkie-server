package site.walkies.walkie.domain.health.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.walkies.walkie.domain.event.service.dto.DailyEggEventResponse;
import site.walkies.walkie.domain.health.service.HealthService;
import site.walkies.walkie.domain.health.service.dto.response.HealthDetailResponseDto;
import site.walkies.walkie.global.auth.dto.MemberPrincipal;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {
    private final HealthService healthService;

    @Operation(
            summary = "날짜별 헬스케어 상세 정보 전달",
            description = "선택한 날짜의 헬스케어 상세 정보를 조회합니다. 쿼리 파라미터로 searchDate를 넘겨줘야합니다."
    )
    @GetMapping("/detail")
    public SuccessResponse<HealthDetailResponseDto> getHealthDetail(@RequestParam(value = "searchDate") LocalDate searchDate, @AuthenticationPrincipal MemberPrincipal principal) {
        return SuccessResponse.ok(
                healthService.getHealthDetail(principal.getMemberId(),searchDate)
        );
    }
}
