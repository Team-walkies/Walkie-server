package site.walkies.walkie.domain.character.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.walkies.walkie.domain.character.entity.UserCharacterBorn;

@Repository
public interface UserCharacterBornRepository extends JpaRepository<UserCharacterBorn, Long> {
    int countByUserCharacterId(Long userCharacterId);
}
