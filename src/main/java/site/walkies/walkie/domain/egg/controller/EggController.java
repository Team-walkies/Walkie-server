package site.walkies.walkie.domain.egg.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.egg.entity.Egg;
import site.walkies.walkie.domain.egg.service.EggService;
import site.walkies.walkie.domain.egg.service.dto.request.CreateEggRequest;
import site.walkies.walkie.domain.egg.service.dto.response.*;
import site.walkies.walkie.domain.egg.service.dto.response.GetEggCountResponse;
import site.walkies.walkie.domain.egg.service.dto.response.GetEggDetailResponse;
import site.walkies.walkie.domain.egg.service.dto.request.PostStepRequest;
import site.walkies.walkie.domain.egg.service.dto.response.GetEggResponse;
import site.walkies.walkie.global.auth.dto.MemberPrincipal;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/eggs")
@RequiredArgsConstructor
public class EggController {
    // service 추가
    private final EggService eggService;

    @Operation(
            summary = "보유한 알 목록 조회",
            description = "사용자가 보유 중인 모든 알 정보를 조회합니다."
    )
    @GetMapping
    public SuccessResponse<EggListResponse> getAll(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        List<GetEggResponse> responses = eggService.getEggsList(memberPrincipal.getMemberId());
        EggListResponse response = new EggListResponse(responses);
        return SuccessResponse.ok(response);
    }

    @Operation(
            summary = "알 걸음 수 업데이트",
            description = "특정 알에 대해 현재까지 걸은 걸음 수를 업데이트합니다. " +
                    "위치 정보(latitude, longitude)도 함께 전송됩니다."
    )
    @PatchMapping("/steps")
    public SuccessResponse<?> updateSteps(@RequestBody PostStepRequest stepRequest) {
        EggResponse response = eggService.updateEggNowStep(stepRequest.getEggId(), stepRequest.getNowStep(),stepRequest.getLatitude(),stepRequest.getLongitude());
        return SuccessResponse.updated(response);
    }

    @Operation(
            summary = "알 상세 정보 조회",
            description = "특정 알의 상세 정보를 조회합니다. 알 ID를 경로 변수로 전달해야 합니다."
    )
    @GetMapping("/{eggId}")
    public SuccessResponse<GetEggDetailResponse> getEggDetail(@PathVariable Long eggId) {
        GetEggDetailResponse response = eggService.getEggDetail(eggId);
        return SuccessResponse.ok(response);
    }

    @Operation(
            summary = "보유한 알 개수 조회",
            description = "사용자가 보유 중인 알의 총 개수를 조회합니다."
    )
    @GetMapping("/count")
    public SuccessResponse<GetEggCountResponse> getEggCount(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        GetEggCountResponse response = eggService.getEggCount(memberPrincipal.getMemberId());
        return SuccessResponse.ok(response);
    }

    @Operation(
            summary = "알 생성 API",
            description = "사용자가 위치 정보를 보내면 알을 생성합니다. (제한이 없습니다.)"
    )
    @PostMapping("")
    public SuccessResponse<?> createEgg(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody CreateEggRequest createEggRequest) {
        EggResponse response = eggService.createEgg(memberPrincipal.getMemberId(),createEggRequest.getLatitude(), createEggRequest.getLongitude());
        return SuccessResponse.ok(response);
    }

    @Operation(
            summary = "헬스케어 목표 달성용 알 생성",
            description = "사용자가 위치 정보를 보내면 알을 생성합니다. (하루에 한번만 알을 지급 받을 수 있습니다.)"
    )
    @PostMapping("/awards")
    public SuccessResponse<?> createHealthCareAwardsEgg(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody CreateEggRequest createEggRequest) {
        EggResponse response = eggService.createHealthCareAwardsEgg(memberPrincipal.getMemberId(),createEggRequest.getLatitude(), createEggRequest.getLongitude(), createEggRequest.getGetHealthEggDate());
        return SuccessResponse.ok(response);
    }
}
