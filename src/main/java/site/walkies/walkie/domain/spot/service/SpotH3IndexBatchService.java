package site.walkies.walkie.domain.spot.service;

import com.uber.h3core.H3Core;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.walkies.walkie.domain.spot.entity.Spot;
import site.walkies.walkie.domain.spot.repository.SpotRepository;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SpotH3IndexBatchService {

    private final SpotRepository spotRepository;
    private static final int H3_RESOLUTION = 9;

    public void updateMissingH3Indexes() {
        log.info("[H3 Index 업데이트 시작]");

        H3Core h3;
        try {
            h3 = H3Core.newInstance();
        } catch (IOException e) {
            log.error("H3Core 초기화 실패", e);
            return;
        }

        List<Spot> spotsWithoutH3 = spotRepository.findByH3IndexIsNull();
        log.info("총 처리 대상 Spot 수: {}", spotsWithoutH3.size());

        int successCount = 0;
        int failCount = 0;

        for (Spot spot : spotsWithoutH3) {
            try {
                double lat = spot.getLatitude();
                double lng = spot.getLongitude();

                String h3Index = h3.geoToH3Address(lat, lng, H3_RESOLUTION);
                spot.changeH3Index(h3Index);
                successCount++;
            } catch (Exception e) {
                log.warn("Spot ID {} 처리 실패 - 위도/경도 문제? ({}, {})", spot.getId(), spot.getLatitude(), spot.getLongitude());
                failCount++;
            }
        }

        log.info("H3 Index 업데이트 완료 - 성공: {}, 실패: {}", successCount, failCount);
    }
}
