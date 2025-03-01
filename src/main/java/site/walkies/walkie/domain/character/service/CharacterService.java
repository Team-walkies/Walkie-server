package site.walkies.walkie.domain.character.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.character.entity.UserCharacterBorn;
import site.walkies.walkie.domain.character.repository.UserCharacterBornRepository;
import site.walkies.walkie.domain.character.repository.UserCharacterRepository;
import site.walkies.walkie.domain.character.service.dto.response.GetCharacterResponse;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.character.entity.UserCharacter;
import site.walkies.walkie.domain.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

        UserCharacter userCharacter = new UserCharacter(rank, type, characterClass,picked, member);
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

    // 캐릭터 리스트 조회 함수
    // input : userId, characterType
    // output : List<GetCharacterResponse>
    public List<GetCharacterResponse> getCharacters(long userId,Integer characterType) {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) { return null; }

        List<UserCharacter> userCharacters;
        
        // 타입을 요청으로 보낸 경우
        if(characterType != null) {
            userCharacters = userCharacterRepository.findAllByUserIdAndType(userId,characterType);
        }
        // 타입을 요청으로 보내지 않은 경우
        else {
            userCharacters = userCharacterRepository.findAllByUserId(userId);
        }


        List<GetCharacterResponse> responses = new ArrayList<>();
        for(UserCharacter userCharacter : userCharacters) {
            // 태어난 캐릭터의 갯수를 확인
            int count = userCharacterBornRepository.countByUserCharacterId(userCharacter.getId());
            // 아직 태어난 캐릭터가 없으면 pass
            if(count == 0) continue;
            GetCharacterResponse response = GetCharacterResponse.builder()
                    .characterId(userCharacter.getId())
                    .type(userCharacter.getType())
                    .characterClass(userCharacter.getCharacterClass())
                    .rank(userCharacter.getRank())
                    .count(count)
                    .picked(userCharacter.getPicked())
                    .build();
            responses.add(response);
        }
        return responses;
    }

}
