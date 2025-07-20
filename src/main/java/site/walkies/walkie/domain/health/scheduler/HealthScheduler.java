package site.walkies.walkie.domain.health.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.walkies.walkie.domain.health.entity.HealthCurrent;
import site.walkies.walkie.domain.health.repository.HealthCurrentRepository;
import site.walkies.walkie.domain.health.service.HealthService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class HealthScheduler {
    private final HealthService healthService;
    private final HealthCurrentRepository healthCurrentRepository;

    @Scheduled(cron = "0 0 2 * * *", zone = "Asia/Seoul")
    public void fallBackArchiveUnrolledMembers() {
        List<HealthCurrent> healthCurrents = healthCurrentRepository.findAll();

        Set<Long> healthCurrentMember = new HashSet<>();
        for (HealthCurrent healthCurrent : healthCurrents) {
            healthCurrentMember.add(healthCurrent.getMember().getId());
        }

        for(Long id : healthCurrentMember) {
            healthService.updateHealthDB(id);
        }
    }
}
