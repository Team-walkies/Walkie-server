package site.walkies.walkie.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.walkies.walkie.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderId(String providerId);

    boolean existsByProviderAndProviderId(String provider, String providerId);

    // 스케쥴러로 매일 삭제할 것들 탐색하기
    List<Member> findAllByDeleteCdTrueAndDeleteRequestedAtBefore(LocalDateTime before);
}
