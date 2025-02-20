package site.walkies.walkie.domain.egg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.walkies.walkie.domain.egg.entity.Egg;

import java.util.List;

@Repository
public interface EggRepository extends JpaRepository<Egg, Long> {
    List<Egg> findAllByUserId(Long userId);
}
