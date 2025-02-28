package site.walkies.walkie.domain.egg.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.egg.entity.Egg;
import site.walkies.walkie.domain.egg.service.EggService;
import site.walkies.walkie.domain.egg.service.dto.request.PostStepRequest;
import site.walkies.walkie.domain.egg.service.dto.response.GetEggResponse;
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
        List<GetEggResponse> responses = eggService.getEggsList(1);
        return SuccessResponse.ok(responses);
    }

    //  알의 걸은 걸음수 업데이트 API
    @PostMapping("/steps")
    public SuccessResponse<?> updateSteps(@RequestBody PostStepRequest stepRequest) {
        eggService.updateEggNowStep(stepRequest.getEggId(), stepRequest.getNowStep(),stepRequest.getLatitude(),stepRequest.getLongitude());
        return SuccessResponse.ok();
    }
}
