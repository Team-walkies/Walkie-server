package site.walkies.walkie.domain.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.walkies.walkie.domain.spot.service.SpotPhotoService;
import site.walkies.walkie.domain.spot.service.SpotSyncService;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class Internal {

    private final SpotSyncService spotSyncService;
    private final SpotPhotoService spotPhotoService;

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
