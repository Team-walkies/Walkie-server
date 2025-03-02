package site.walkies.walkie.domain.character.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.walkies.walkie.domain.character.entity.UserCharacter;

import java.util.List;

@Repository
public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {

    // 유저가 소유한 캐릭터 조회
    List<UserCharacter> findAllByUserId(Long userId);

    // 해당 타입의 유저가 소유한 캐릭터 조회
    List<UserCharacter> findAllByUserIdAndType(Long userId, Integer type);

    UserCharacter findByUserIdAndRankAndAndTypeAndAndCharacterClass(long userId, int rank, int type, int characterClass);

}
