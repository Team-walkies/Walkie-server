package site.walkies.walkie.domain.egg.repository;

import org.springframework.data.repository.CrudRepository;
import site.walkies.walkie.domain.egg.entity.HealthEgg;

import java.util.Optional;

public interface HealthEggRepository extends CrudRepository<HealthEgg, Long> {
    Optional<HealthEgg> findByMemberId(Long memberId);
}
