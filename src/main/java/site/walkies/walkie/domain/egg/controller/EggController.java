package site.walkies.walkie.domain.egg.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.egg.service.EggService;
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

    // 보유한 알 리스트 조회 API
    @GetMapping
    public SuccessResponse<EggListResponse> getAll(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        List<GetEggResponse> responses = eggService.getEggsList(memberPrincipal.getMemberId());
        EggListResponse response = new EggListResponse(responses);
        return SuccessResponse.ok(response);
    }

    //  알의 걸은 걸음수 업데이트 API
    @PatchMapping("/steps")
    public SuccessResponse<?> updateSteps(@RequestBody PostStepRequest stepRequest) {
        EggResponse response = eggService.updateEggNowStep(stepRequest.getEggId(), stepRequest.getNowStep(),stepRequest.getLatitude(),stepRequest.getLongitude());
        // update 추가시 수정 필요
        return SuccessResponse.ok(response);
    }

    // 알 디테일 조회 API
    @GetMapping("/{eggId}")
    public SuccessResponse<GetEggDetailResponse> getEggDetail(@PathVariable Long eggId) {
        GetEggDetailResponse response = eggService.getEggDetail(eggId);
        return SuccessResponse.ok(response);
    }

    // 보유한 알 갯수 조회 API
    @GetMapping("/count")
    public SuccessResponse<GetEggCountResponse> getEggCount(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        GetEggCountResponse response = eggService.getEggCount(memberPrincipal.getMemberId());
        return SuccessResponse.ok(response);
    }
}
