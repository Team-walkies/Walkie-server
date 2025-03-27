package site.walkies.walkie.domain.spot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.walkies.walkie.domain.spot.entity.SpotPhoto;

import java.util.List;

@Repository
public interface SpotPhotoRepository extends JpaRepository<SpotPhoto, Long> {
    List<SpotPhoto> findBySpotId(Long spotId);
}
