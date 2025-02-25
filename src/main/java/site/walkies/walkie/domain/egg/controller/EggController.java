package site.walkies.walkie.domain.egg.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.walkies.walkie.domain.egg.entity.Egg;
import site.walkies.walkie.domain.egg.service.EggService;
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
    public List<GetEggResponse> getAll() {
        List<GetEggResponse> responses = eggService.getEggsList(1);
        return responses;
    }

    // 알 생성 테스트 API
    @PostMapping
    public SuccessResponse<?> createEgg() {
        Egg egg = eggService.createEgg(1, "테스트 장소", LocalDate.now());
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
