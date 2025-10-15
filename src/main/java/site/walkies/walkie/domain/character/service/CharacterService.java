package site.walkies.walkie.domain.character.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.character.entity.UserCharacterBorn;
import site.walkies.walkie.domain.character.repository.UserCharacterBornRepository;
import site.walkies.walkie.domain.character.repository.UserCharacterRepository;
import site.walkies.walkie.domain.character.service.dto.response.GetCharacterCount;
import site.walkies.walkie.domain.character.service.dto.response.GetCharacterDetailResponse;
import site.walkies.walkie.domain.character.service.dto.response.GetCharacterResponse;
import site.walkies.walkie.domain.character.service.dto.response.ObtainedDetail;
import site.walkies.walkie.domain.egg.entity.Egg;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.character.entity.UserCharacter;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.global.Tmap.TmapAPIService;
import site.walkies.walkie.global.probability.CharacterProbability;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final UserCharacterRepository userCharacterRepository;
    private final UserCharacterBornRepository userCharacterBornRepository;
    private final MemberRepository memberRepository;

    private final TmapAPIService tmapAPIService;

    // 캐릭터 생성 함수
    // input : userId, rank(캐릭터 등급), type(캐릭터 타입), characterClass(캐릭터 소분류), picked(같이 다니는 여부)
    // output : character
    public UserCharacter createCharacter(Long userId, int rank, int type, int characterClass, boolean picked) {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

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

    // 기본 캐릭터 생성 함수
    // input : userId, createDate(생성일), latitude, longitude (생성 위경도)
    // output : x
    public UserCharacter createDefaultCharacter(long userId, LocalDate createDate) {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 기본 캐릭터 생성 후 리턴
        UserCharacter defaultUserCharacter = new UserCharacter(0, 0, 0,true, member);
        defaultUserCharacter = userCharacterRepository.save(defaultUserCharacter);

        // 회원가입시 기본 캐릭터 틀 전부 생성
        addUserCharacter(member);

        // 기본 캐릭터 부화
        createCharacterBorn(defaultUserCharacter.getId(), createDate, "탄생의 바다");

        return defaultUserCharacter;
    }

    // 캐릭터 부화 함수
    // input : characterId, obtainDate(부화 날짜), obtainPosition(부화 위치)
    // output : CharacterBorn
    public UserCharacterBorn createCharacterBorn(Long characterId, LocalDate obtainedDate, String obtainedPosition) {
        UserCharacter userCharacter = userCharacterRepository.findById(characterId).orElse(null);
        if(userCharacter == null) {
            throw new CustomException(ErrorCode.CHARACTER_NOT_FOUND);
        }

        UserCharacterBorn userCharacterBorn = new UserCharacterBorn(obtainedDate, obtainedPosition, userCharacter);
        userCharacterBornRepository.save(userCharacterBorn);

        return userCharacterBorn;
    }

    // 캐릭터 리스트 조회 함수
    // input : userId, characterType
    // output : List<GetCharacterResponse>
    public List<GetCharacterResponse> getCharacters(long userId,Integer characterType) {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

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
            // 캐릭터가 없어도 모든 캐릭터 정보 제공
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

    // 보유한 캐릭터 갯수 조회 API
    // input : userId
    // output : GetCharacterCount
    public GetCharacterCount getCharacterCount(long userId) {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        List<UserCharacter> userCharacters = userCharacterRepository.findAllByUserId(userId);;

        int count = 0;
        for(UserCharacter userCharacter : userCharacters) {
            count += userCharacterBornRepository.countByUserCharacterId(userCharacter.getId());
        }
        GetCharacterCount response = GetCharacterCount.builder()
                .charactersCount(count)
                .build();
        return response;
    }

    // 캐릭터 획득 정보 상세 조회 함수
    // input : characterId
    // output : GetCharacterDetailResponse
    public GetCharacterDetailResponse getCharacterDetailResponse(Long characterId) {
        UserCharacter userCharacter = userCharacterRepository.findById(characterId).orElse(null);
        if(userCharacter == null) {
            throw new CustomException(ErrorCode.CHARACTER_NOT_FOUND);
        }
        // 해당 캐릭터 갯수
        int count = userCharacterBornRepository.countByUserCharacterId(userCharacter.getId());
        // 캐릭터 부화 리스트 조회
        List<UserCharacterBorn> userCharacterBorns = userCharacterBornRepository.findAllByUserCharacterId(userCharacter.getId());
        // 캐릭터 부화 디테일 저장
        List<ObtainedDetail> details = new ArrayList<>();
        for(UserCharacterBorn userCharacterBorn : userCharacterBorns) {
            ObtainedDetail detail = ObtainedDetail.builder()
                    .obtainedPosition(userCharacterBorn.getObtainedPosition())
                    .obtainedDate(userCharacterBorn.getObtainedDate())
                    .build();
            details.add(detail);
        }

        // 추후 개선 필요 => 캐릭터 검색이 아닌 DB에 캐릭터 enum 저장 필요
        CharacterProbability cp = Arrays.stream(CharacterProbability.values())
                .filter(e -> e.getRank() == userCharacter.getRank()
                        && e.getType() == userCharacter.getType()
                        && e.getCharacterClass() == userCharacter.getCharacterClass())
                .findFirst().orElseThrow(() -> new CustomException(ErrorCode.CHARACTER_TYPE_NOT_FOUND));;


        GetCharacterDetailResponse response = GetCharacterDetailResponse.builder()
                .characterCount(count)
                .rank(userCharacter.getRank())
                .characterClass(userCharacter.getCharacterClass())
                .type(userCharacter.getType())
                .characterName(cp.getName())
                .characterDescription(cp.getDetail())
                .characterImageUrl("https://walkie.site/api/v1/file/" + cp.getPicUrl() + ".png")
                .obtainedDetails(details)
                .build();
        return response;
    }

    // 모든 유저 빈 캐릭터 추가 함수 => 모든 유저의 데이터를 확인해서 없는 캐릭터 유형을 추가
    // input : x
    // output : x
    public void addAllUserCharacter() {
        // 모든 유저의 데이터 확인
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            addUserCharacter(member);
        }
    }

    // 특정 유저 빈 캐릭터 생성 함수 => 특정 유저의 데이터를 확인해서 없는 캐릭터 유형을 추가
    // input : Member
    // output : x
    private void addUserCharacter(Member member) {
        for (CharacterProbability cp : CharacterProbability.values()) {
            createCharacter(
                    member.getId(),
                    cp.getRank(),
                    cp.getType(),
                    cp.getCharacterClass(),
                    false
            );
        }
    }

    // 유저 id를 통한 캐릭터 및 캐릭터 born 완전 삭제
    // input : userId
    // output : x
    public void deleteEggByUserId(long userId) {
        List<UserCharacter> characters = userCharacterRepository.findAllByUserId(userId);
        for(UserCharacter userCharacter : characters) {
            List<UserCharacterBorn> userCharacterBorns = userCharacterBornRepository.findAllByUserCharacterId(userCharacter.getId());
            for(UserCharacterBorn userCharacterBorn : userCharacterBorns) {
                userCharacterBornRepository.delete(userCharacterBorn);
            }
            userCharacterRepository.delete(userCharacter);
        }
    }
}
