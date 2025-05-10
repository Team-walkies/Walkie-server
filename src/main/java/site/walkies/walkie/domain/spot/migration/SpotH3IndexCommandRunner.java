package site.walkies.walkie.domain.spot.migration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import site.walkies.walkie.domain.spot.service.SpotH3IndexBatchService;

// NOTE: 현재는 사용되지 않음. Spot h3_index 수동 보정용 배치 로직 (CommandLineRunner)
// 사용 시 @Component 활성화 필요
// @Component
@RequiredArgsConstructor
public class SpotH3IndexCommandRunner implements CommandLineRunner {

    private final SpotH3IndexBatchService spotH3IndexBatchService;

    @Override
    public void run(String... args) {
        System.out.println("🌐 Spot H3 Index 배치 실행 시작");
        spotH3IndexBatchService.updateMissingH3Indexes();
        System.out.println("✅ Spot H3 Index 배치 실행 완료");
    }
}
