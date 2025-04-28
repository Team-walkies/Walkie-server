package site.walkies.walkie.domain.spot.migration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.walkies.walkie.domain.spot.entity.Spot;
import site.walkies.walkie.domain.spot.enums.SpotKeyword;
import site.walkies.walkie.domain.spot.repository.SpotRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpotKeywordMigrationRunner implements ApplicationRunner {

    private final SpotRepository spotRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        log.info("▶️ Spot Keyword Migration 시작");

        var spots = spotRepository.findAll();

        for (Spot spot : spots) {
            SpotKeyword keyword = spot.getKeyword();

            if (keyword == null) {
                log.warn("⚠️ Spot ID {}: keyword가 null입니다. 건너뜀", spot.getId());
                continue;
            }

            switch (keyword.name()) {
                case "PARK" -> {
                    spot.changeKeyword(SpotKeyword.NATURE);
                    log.info("✅ Spot ID {}: PARK -> NATURE", spot.getId());
                }
                case "CAFE" -> {
                    spot.changeKeyword(SpotKeyword.FOOD);
                    log.info("✅ Spot ID {}: CAFE -> FOOD", spot.getId());
                }
                case "ETC" -> {
                    spot.changeKeyword(SpotKeyword.HUMANITIES);
                    log.info("✅ Spot ID {}: ETC -> HUMANITIES", spot.getId());
                }
                default -> {
                    log.warn("⚠️ Spot ID {}: 예상치 못한 keyword {}", spot.getId(), keyword);
                }
            }
        }

        log.info("🏁 Spot Keyword Migration 완료");
    }
}
