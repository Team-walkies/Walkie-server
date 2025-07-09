package site.walkies.walkie.domain.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.walkies.walkie.domain.health.entity.HealthHistory;

import java.time.LocalDate;
import java.util.Optional;

public interface HealthHistoryRepository extends JpaRepository<HealthHistory, Long> {
    Optional<HealthHistory> findByMemberIdAndRecordDate(Long memberId, LocalDate recordDate);
}
