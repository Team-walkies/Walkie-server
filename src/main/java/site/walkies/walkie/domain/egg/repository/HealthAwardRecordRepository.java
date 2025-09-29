package site.walkies.walkie.domain.egg.repository;

import org.springframework.data.repository.CrudRepository;
import site.walkies.walkie.domain.egg.entity.HealthAwardRecord;
import site.walkies.walkie.domain.member.entity.Member;

import java.time.LocalDate;
import java.util.Optional;

public interface HealthAwardRecordRepository extends CrudRepository<HealthAwardRecord, Long> {
    Optional<HealthAwardRecord> findByMemberIdAndReceivedDate(Long memberId, LocalDate receivedDate);

    Long member(Member member);
}
