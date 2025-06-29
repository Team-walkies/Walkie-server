// DailyEggEventRepository.java
package site.walkies.walkie.domain.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.walkies.walkie.domain.event.entity.DailyEggEvent;
import site.walkies.walkie.domain.member.entity.Member;

import java.util.Optional;

public interface DailyEggEventRepository extends JpaRepository<DailyEggEvent, Long> {
    Optional<DailyEggEvent> findByMember(Member member);
}
