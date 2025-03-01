package site.walkies.walkie.domain.egg.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.egg.entity.Egg;
import site.walkies.walkie.domain.egg.service.EggService;
import site.walkies.walkie.domain.egg.service.dto.response.*;
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
    public SuccessResponse<EggListResponse> getAll() {
        List<GetEggResponse> responses = eggService.getEggsList(1);
        EggListResponse response = new EggListResponse(responses);
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
    public SuccessResponse<GetEggCountResponse> getEggCount() {
        GetEggCountResponse response = eggService.getEggCount(1);
        return SuccessResponse.ok(response);
    }
}
