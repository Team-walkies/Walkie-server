package site.walkies.walkie.domain.character.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.walkies.walkie.domain.character.entity.UserCharacter;

@Repository
public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {
    UserCharacter findByUserIdAndRankAndAndTypeAndAndCharacterClass(long userId, int rank, int type, int characterClass);
}
