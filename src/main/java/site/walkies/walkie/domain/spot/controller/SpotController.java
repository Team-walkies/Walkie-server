package site.walkies.walkie.domain.spot.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.spot.service.SpotPhotoService;
import site.walkies.walkie.domain.spot.service.SpotService;
import site.walkies.walkie.domain.spot.service.SpotSyncService;
import site.walkies.walkie.domain.spot.service.dto.request.SpotNearbyRequestDto;
import site.walkies.walkie.domain.spot.service.dto.response.SpotNearbyResponseDto;
import site.walkies.walkie.domain.spot.service.dto.response.SpotResponseDto;
import site.walkies.walkie.global.auth.dto.MemberPrincipal;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;
import site.walkies.walkie.global.web.exception.CustomException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spots")
public class SpotController {
    private final SpotService spotService;
    private final SpotSyncService spotSyncService;
    private final SpotPhotoService spotPhotoService;

    @GetMapping("/{spotId}")
    public SuccessResponse<SpotResponseDto> getSpotInfo(
            @PathVariable Long spotId,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        SpotResponseDto responseDto = spotService.getSpotInfo(spotId, memberPrincipal.getMemberId());
        return SuccessResponse.ok(responseDto);
    }

    @PostMapping("/nearby")
    public SuccessResponse<List<SpotNearbyResponseDto>> getNearbySpots(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody SpotNearbyRequestDto request
    ) {
        List<SpotNearbyResponseDto> response = spotService.getNearbySpots(request, memberPrincipal.getMemberId());
        return SuccessResponse.ok(response);
    }

    @Operation(
            summary = "spot 생성 api",
            description = "tour api에서 spot을 가져옵니다."
    )
    @GetMapping("/generate")
    public SuccessResponse<?> generateSpot() {
        spotSyncService.syncAllSpots();
        return SuccessResponse.ok();
    }

    @Operation(
            summary = "상세 url 및 사진 생성 api",
            description = "상세 url과 spot의 photo를 크롤링해옵니다."
    )
    @GetMapping("/generate/photo1")
    public SuccessResponse<?> getSpotPhoto1() {
        spotPhotoService.enrichSpotPhotos();
        return SuccessResponse.ok();
    }

    @Operation(
            summary = "사진만 생성 api",
            description = "상세 url이 있는 spot의 photo를 크롤링해옵니다."
    )
    @GetMapping("/generate/photo2")
    public SuccessResponse<?> getSpotPhoto2() {
        spotPhotoService.enrichPhotosFromDetailUrls();
        return SuccessResponse.ok();
    }
}
