package site.walkies.walkie.domain.egg.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.egg.entity.Egg;
import site.walkies.walkie.domain.egg.service.EggService;
import site.walkies.walkie.domain.egg.service.dto.response.GetEggCountResponse;
import site.walkies.walkie.domain.egg.service.dto.response.GetEggDetailResponse;
import site.walkies.walkie.domain.egg.service.dto.request.PostStepRequest;
import site.walkies.walkie.domain.egg.service.dto.response.GetEggResponse;
import site.walkies.walkie.domain.egg.service.dto.response.PostEggResponse;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/eggs")
@RequiredArgsConstructor
public class EggController {
    // service 추가
    private final EggService eggService;

    // 보유한 알 리스트 조회 API
    @GetMapping
    public SuccessResponse<List<GetEggResponse>> getAll() {
        List<GetEggResponse> responses = eggService.getEggsList(2);
        return SuccessResponse.ok(responses);
    }

    //  알의 걸은 걸음수 업데이트 API
    @PostMapping("/steps")
    public SuccessResponse<?> updateSteps(@RequestBody PostStepRequest stepRequest) {
        eggService.updateEggNowStep(stepRequest.getEggId(), stepRequest.getNowStep(),stepRequest.getLatitude(),stepRequest.getLongitude());
        return SuccessResponse.ok();
    }

    // 알 디테일 조회 API
    @GetMapping("/{eggId}")
    public SuccessResponse<GetEggDetailResponse> getEggDetail(@PathVariable Long eggId) {
        GetEggDetailResponse response = eggService.getEggDetail(eggId);
        return SuccessResponse.ok(response);
    }

    // 보유한 알 갯수 조회 API
    @GetMapping("/count")
    public SuccessResponse<GetEggCountResponse> getEggCount() {
        GetEggCountResponse response = eggService.getEggCount(2);
        return SuccessResponse.ok(response);
    }

    @PostMapping
    public SuccessResponse<?> createEgg() {
        Egg egg = eggService.createEgg(2, "테스트 장소", LocalDate.now());
        PostEggResponse postEggResponse = PostEggResponse.builder()
                .eggId(egg.getId())
                .rank(egg.getRank())
                .needStep(egg.getNeedStep())
                .nowStep(egg.getNowStep())
                .obtainedPosition(egg.getObtainedPosition())
                .obtainedDate(egg.getObtainedDate())
                .picked(egg.getPicked())
                .userCharacterId(egg.getUserCharacter().getId())
                .memberId(egg.getUser().getId())
                .build();
        return SuccessResponse.created(postEggResponse);
    }
}
