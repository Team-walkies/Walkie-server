package site.walkies.walkie.domain.character.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.character.entity.UserCharacterBorn;
import site.walkies.walkie.domain.character.repository.UserCharacterBornRepository;
import site.walkies.walkie.domain.character.repository.UserCharacterRepository;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.character.entity.UserCharacter;
import site.walkies.walkie.domain.member.repository.MemberRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final UserCharacterRepository userCharacterRepository;
    private final UserCharacterBornRepository userCharacterBornRepository;
    private final MemberRepository memberRepository;

    // 캐릭터 생성 함수
    // input : userId, rank(캐릭터 등급), type(캐릭터 타입), characterClass(캐릭터 소분류), picked(같이 다니는 여부)
    // output : character
    public UserCharacter createCharacter(Long userId, int rank, int type, int characterClass, boolean picked) {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) { return null; }

        // 캐릭터가 존재하는 경우 캐릭터 return
        UserCharacter userCharacter = userCharacterRepository.findByUserIdAndRankAndAndTypeAndAndCharacterClass(member.getId(),rank,type,characterClass);
        if (userCharacter != null) {
            return userCharacter;
        }

        // 없는 경우 생성  후 return
        userCharacter = new UserCharacter(rank, type, characterClass,picked, member);
        userCharacterRepository.save(userCharacter);

        return userCharacter;
    }

    // 캐릭터 부화 함수
    // input : characterId, obtainDate(부화 날짜), obtainPosition(부화 위치)
    // output : CharacterBorn
    public UserCharacterBorn createCharacterBorn(Long characterId, LocalDate obtainedDate, String obtainedPosition) {
        UserCharacter userCharacter = userCharacterRepository.findById(characterId).orElse(null);
        if(userCharacter == null) { return null;}

        UserCharacterBorn userCharacterBorn = new UserCharacterBorn(obtainedDate, obtainedPosition, userCharacter);
        userCharacterBornRepository.save(userCharacterBorn);

        return userCharacterBorn;
    }

}
