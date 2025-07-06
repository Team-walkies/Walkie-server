package site.walkies.walkie.domain.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.walkies.walkie.domain.health.entity.HealthHistory;

public interface HealthHistoryRepository extends JpaRepository<HealthHistory, Long> {
}
