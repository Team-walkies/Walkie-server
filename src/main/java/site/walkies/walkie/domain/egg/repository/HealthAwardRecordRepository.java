package site.walkies.walkie.domain.egg.repository;

import org.springframework.data.repository.CrudRepository;
import site.walkies.walkie.domain.egg.entity.HealthAwardRecord;

import java.util.Optional;

public interface HealthAwardRecordRepository extends CrudRepository<HealthAwardRecord, Long> {
    Optional<HealthAwardRecord> findByMemberId(Long memberId);
}
