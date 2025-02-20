package site.walkies.walkie.domain.egg.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.walkies.walkie.domain.egg.service.dto.EggService;
import site.walkies.walkie.domain.egg.service.dto.response.GetEggResponse;

import java.util.List;

@RestController
@RequestMapping("/eggs")
@RequiredArgsConstructor
public class EggController {
    // service 추가
    private final EggService eggService;

    // 보유한 알 리스트 조회 API
    @GetMapping
    public List<GetEggResponse> getAll() {
        List<GetEggResponse> responses = eggService.getEggsList(1);
        return responses;
    }
}
