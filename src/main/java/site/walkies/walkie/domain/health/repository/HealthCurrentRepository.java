package site.walkies.walkie.domain.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.walkies.walkie.domain.health.entity.HealthCurrent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HealthCurrentRepository extends JpaRepository<HealthCurrent, Long> {
//    Optional<HealthCurrent> findByMemberId(Long memberId);
    Optional<HealthCurrent> findByMemberIdAndNowDay(Long memberId, LocalDate nowDay);

//    @Query("""
//        SELECT hc
//        FROM HealthCurrent hc
//        WHERE hc.member.id = :memberId
//          AND hc.nowDay < :before
//        ORDER BY hc.nowDay DESC
//        limit 1
//    """)
    // limit 1 오류로 인한 변경
    Optional<HealthCurrent> findFirstByMemberIdAndNowDayBeforeOrderByNowDayDesc(@Param("memberId") Long memberId,
                                                 @Param("before") LocalDate before);

    // 모든 current(해당 회원) 조회 - 날짜 오름차순
    List<HealthCurrent> findAllByMemberIdOrderByNowDayAsc(Long memberId);
}
