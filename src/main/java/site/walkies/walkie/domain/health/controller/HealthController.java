package site.walkies.walkie.domain.health.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.event.service.dto.DailyEggEventResponse;
import site.walkies.walkie.domain.health.service.HealthService;
import site.walkies.walkie.domain.health.service.dto.request.HealthMoveUpdateRequestDto;
import site.walkies.walkie.domain.health.service.dto.response.HealthDetailResponseDto;
import site.walkies.walkie.domain.health.service.dto.response.HealthMoveResponseDto;
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

    @Operation(
            summary = "오늘 헬스케어 정보 업데이트",
            description = "헬스케어 정보를 업데이트 합니다. 목표 걸음수, 현재 걸음수, 현재 거리, 현재 칼로리를 업데이트 가능합니다."
    )
    @PostMapping("/")
    public SuccessResponse<HealthMoveResponseDto> updateHealthDetail(@AuthenticationPrincipal MemberPrincipal principal, @RequestBody HealthMoveUpdateRequestDto requestDto) {
        return SuccessResponse.ok(
                healthService.updateHealthDetail(principal.getMemberId(), requestDto.getTargetSteps(), requestDto.getNowSteps(), requestDto.getNowDistance(), requestDto.getNowCalories())
        );
    }
}
