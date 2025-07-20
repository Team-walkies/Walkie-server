package site.walkies.walkie.domain.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.walkies.walkie.domain.health.entity.HealthCurrent;

import java.time.LocalDate;
import java.util.Optional;

public interface HealthCurrentRepository extends JpaRepository<HealthCurrent, Long> {
//    Optional<HealthCurrent> findByMemberId(Long memberId);
    Optional<HealthCurrent> findByMemberIdAndNowDay(Long memberId, LocalDate nowDay);

    @Query("""
        SELECT hc
        FROM HealthCurrent hc
        WHERE hc.member.id = :memberId
          AND hc.nowDay < :before
        ORDER BY hc.nowDay DESC
        limit 1
    """)
    Optional<HealthCurrent> findLatestBeforeDate(@Param("memberId") Long memberId,
                                                 @Param("before") LocalDate before);
}
