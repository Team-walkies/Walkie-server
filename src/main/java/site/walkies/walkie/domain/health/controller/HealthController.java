package site.walkies.walkie.domain.health.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.event.service.dto.DailyEggEventResponse;
import site.walkies.walkie.domain.health.service.HealthService;
import site.walkies.walkie.domain.health.service.dto.request.HealthMoveUpdateRequestDto;
import site.walkies.walkie.domain.health.service.dto.response.HealthContinueDayResponseDto;
import site.walkies.walkie.domain.health.service.dto.response.HealthDetailResponseDto;
import site.walkies.walkie.domain.health.service.dto.response.HealthMoveResponseDto;
import site.walkies.walkie.domain.health.service.dto.response.HealthResponseDto;
import site.walkies.walkie.global.auth.dto.MemberPrincipal;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

import java.time.LocalDate;
import java.util.List;

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
            summary = "기간 내 헬스케어 정보 전달",
            description = "선택한 기간의 헬스케어 정보를 조회합니다. 쿼리 파라미터로 startDate, endDate를 넘겨줘야합니다."
    )
    @GetMapping("")
    public SuccessResponse<List<HealthResponseDto>> getHealthList(@RequestParam(value = "startDate") LocalDate startDate,@RequestParam(value = "endDate") LocalDate endDate,  @AuthenticationPrincipal MemberPrincipal principal) {
        return SuccessResponse.ok(
                healthService.getHealthList(principal.getMemberId(),startDate,endDate)
        );
    }

    @Operation(
            summary = "어제까지의 헬스케어 연속 달성 일수 전달",
            description = "어제까지의 헬스케어 연속 달성 일수를 제공합니다. ex) 2일전, 1일전 목표 달성 => 2 제공"
    )
    @GetMapping("/continueDays")
    public SuccessResponse<HealthContinueDayResponseDto> getContinueHealthDays(@AuthenticationPrincipal MemberPrincipal principal) {
        return SuccessResponse.ok(
                healthService.getHealthContinueDay(principal.getMemberId())
        );
    }

    @Operation(
            summary = "오늘 헬스케어 정보 업데이트",
            description = "헬스케어 정보를 업데이트 합니다. 목표 걸음수, 현재 걸음수, 현재 거리, 현재 칼로리를 업데이트 가능합니다. (오늘 날짜를 보내야합니다.)"
    )
    @PutMapping("")
    public SuccessResponse<HealthMoveResponseDto> updateHealthDetail(@AuthenticationPrincipal MemberPrincipal principal, @RequestBody HealthMoveUpdateRequestDto requestDto) {
        return SuccessResponse.ok(
                healthService.updateHealthDetail(principal.getMemberId(), requestDto.getTargetSteps(), requestDto.getNowSteps(), requestDto.getNowDistance(), requestDto.getNowCalories(), requestDto.getNowDay())
        );
    }

    // current DB에 날짜 추가로 api로 업데이트 불필요
//    @Operation(
//            summary = "오늘 헬스케어 정보 과거 기록 DB로 이동",
//            description = "12시가 지난 시점에 다른 api 호출 전에 반드시 1번 호출이 필요합니다. (새벽 2시에 이전 작업을 위한 배치는 따로 동작을 합니다.)"
//    )
//    @PutMapping("/yesterdayData")
//    public SuccessResponse<?> updateDB(@AuthenticationPrincipal MemberPrincipal principal) {
//        healthService.updateHealthDB(principal.getMemberId());
//        return SuccessResponse.ok();
//    }
}
