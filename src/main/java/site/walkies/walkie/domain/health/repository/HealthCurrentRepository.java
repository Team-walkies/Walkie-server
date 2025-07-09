package site.walkies.walkie.domain.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.walkies.walkie.domain.health.entity.HealthCurrent;

import java.util.Optional;

public interface HealthCurrentRepository extends JpaRepository<HealthCurrent, Long> {
    Optional<HealthCurrent> findByMemberId(Long memberId);
}
