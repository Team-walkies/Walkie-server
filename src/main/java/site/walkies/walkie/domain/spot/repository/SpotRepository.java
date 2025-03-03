package site.walkies.walkie.domain.spot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.walkies.walkie.domain.spot.entity.Spot;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {
}
