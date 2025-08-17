package site.walkies.walkie.domain.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.walkies.walkie.domain.health.entity.HealthHistory;

import java.time.LocalDate;
import java.util.Optional;

public interface HealthHistoryRepository extends JpaRepository<HealthHistory, Long> {
    Optional<HealthHistory> findByMemberIdAndRecordDate(Long memberId, LocalDate recordDate);

    //    @Query("""
//        SELECT hh
//        FROM HealthHistory hh
//        WHERE hh.member.id = :memberId
//          AND hh.recordDate < :before
//        ORDER BY hh.recordDate DESC
//       limit 1
//    """)
    // limit 1 문제로 인한 변경
    Optional<HealthHistory> findFirstByMemberIdAndRecordDateBeforeOrderByRecordDateDesc(@Param("memberId") Long memberId,
                                                                                        @Param("before") LocalDate before);
}