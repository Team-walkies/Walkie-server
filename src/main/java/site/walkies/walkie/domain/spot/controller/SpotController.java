package site.walkies.walkie.domain.spot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.walkies.walkie.domain.spot.service.SpotService;
import site.walkies.walkie.domain.spot.service.dto.response.SpotResponseDto;
import site.walkies.walkie.global.auth.dto.MemberPrincipal;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spots")
public class SpotController {
    private final SpotService spotService;

    @GetMapping("/{spotId}")
    public SuccessResponse<SpotResponseDto> getSpotInfo(
            @PathVariable Long spotId,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        SpotResponseDto responseDto = spotService.getSpotInfo(spotId, memberPrincipal.getMemberId());
        return SuccessResponse.ok(responseDto);
    }
}
