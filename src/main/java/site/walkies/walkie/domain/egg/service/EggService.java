package site.walkies.walkie.domain.egg.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import site.walkies.walkie.domain.character.entity.UserCharacter;
import site.walkies.walkie.domain.character.service.CharacterService;
import site.walkies.walkie.domain.egg.entity.Egg;
import site.walkies.walkie.domain.egg.repository.EggRepository;
import site.walkies.walkie.domain.egg.service.dto.response.*;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.global.Tmap.TmapAPIService;
import site.walkies.walkie.global.probability.CharacterProbability;
import site.walkies.walkie.global.probability.EggsProbability;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EggService {

    private final EggRepository eggRepository;
    private final MemberRepository memberRepository;

    private final CharacterService characterService;
    private final TmapAPIService tmapAPIService;

    // 보유한 알 리스트 조회 method
    // input : user ID
    // output : List<getEggListResponse>
    public List<GetEggResponse> getEggsList(long userId) {
        List<GetEggResponse> eggs = new ArrayList<>();

        for (Egg egg : eggRepository.findAllByUserId(userId)) {
            GetEggResponse getEggResponse = GetEggResponse.createGetEggResponse(egg.getId(),egg.getRank(),egg.getNeedStep(),egg.getNowStep(),egg.getUserCharacter().getId(),egg.getPicked(),egg.getObtainedPosition(),egg.getObtainedDate());
            eggs.add(getEggResponse);
        }

        return eggs;
    }

    // egg 생성 method
    // input : userId, obtainedPosition(얻은 위치), obtainedDate(얻은 날짜)
    // output : Egg
    public Egg createEgg(long userId, String obtainedPosition, LocalDate obtainedDate) {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 1. 랜덤값 생성
        // 알 랭크 랜덤값
        double eggRandom = Math.random() * 100;
        // 캐릭터 등급 랜덤값
        double characterRandom = Math.random() * 100;
        // 같은 등급의 캐릭터 종류 랜덤값
        double characterClassRandom = Math.random() * 100;

        // 2. Egg 타입별 분기 처리 (각 알의 확률 값에 따라)
        if (eggRandom <= EggsProbability.NORMAL_EGG.getProbability()) {
            return processEgg(userId, obtainedPosition, obtainedDate, member, EggsProbability.NORMAL_EGG, characterRandom, characterClassRandom, 2000);
        } else if (eggRandom <= EggsProbability.RARE_EGG.getProbability()) {
            return processEgg(userId, obtainedPosition, obtainedDate, member, EggsProbability.RARE_EGG, characterRandom, characterClassRandom, 6000);
        } else if (eggRandom <= EggsProbability.EPIC_EGG.getProbability()) {
            return processEgg(userId, obtainedPosition, obtainedDate, member, EggsProbability.EPIC_EGG, characterRandom, characterClassRandom, 8000);
        } else if (eggRandom <= EggsProbability.LEGENDARY_EGG.getProbability()) {
            return processEgg(userId, obtainedPosition, obtainedDate, member, EggsProbability.LEGENDARY_EGG, characterRandom, characterClassRandom, 10000);
        }
        return null;
    }

    // 주어진 알의  캐릭터 확률을 기반으로 후보 배열을 선택하고 Egg를 생성하는 함수
    // input : userId, obtainedPosition(얻은 위치), obtainedDate(얻은 날짜), Member, eggProb(알의 등급), characterRandom(캐릭터 등급 랜덤 값), characterClassRandom(캐릭터 종류 랜덥값),eggWalk(필요 걸음수)
    // output : Egg
    private Egg processEgg(long userId, String obtainedPosition, LocalDate obtainedDate, Member member,
                           EggsProbability eggProb, double characterRandom, double characterClassRandom, int eggWalk) {
        // 캐릭터 종류 후보 배열
        CharacterProbability[] candidates;

        // characterRandom을 통해서 캐릭터 등급을 고르고, 각 등급의 캐릭터 종류 후보 배열 생성
        // 일반 등급
        if (characterRandom <= eggProb.getNormalProbability()) {
            candidates = new CharacterProbability[] {
                    CharacterProbability.JELLYFISH,
                    CharacterProbability.RED_JELLYFISH,
                    CharacterProbability.GREEN_JELLYFISH,
                    CharacterProbability.PURPLE_JELLYFISH,
                    CharacterProbability.PINK_JELLYFISH,
                    CharacterProbability.DINO,
                    CharacterProbability.RED_DINO,
                    CharacterProbability.GREEN_DINO,
                    CharacterProbability.PURPLE_DINO,
                    CharacterProbability.PINK_DINO
            };
        }
        // 레어 등급
        else if (characterRandom <= eggProb.getRareProbability()) {
            candidates = new CharacterProbability[] {
                    CharacterProbability.RABBIT_JELLYFISH,
                    CharacterProbability.STARFISH_JELLYFISH,
                    CharacterProbability.DEER_DINO,
                    CharacterProbability.NESSIE_DINO
            };
        }
        // 에픽 등급
        else if (characterRandom <= eggProb.getEpicProbability()) {
            candidates = new CharacterProbability[] {
                    CharacterProbability.LIGHTNING_JELLYFISH,
                    CharacterProbability.STRAWBERRY_JELLYFISH,
                    CharacterProbability.PANCAKE_DINO,
                    CharacterProbability.MELONSODA_DINO
            };
        }
        // 레전더리 등급
        else {
            candidates = new CharacterProbability[] {
                    CharacterProbability.SPACE_JELLYFISH,
                    CharacterProbability.DRAGON_DINO
            };
        }

        // 후보 배열에서 characterClassRandom 값에 따라 캐릭터(enum) 선택
        CharacterProbability selectedCandidate = selectCandidate(candidates, characterClassRandom);

        // 캐릭터 생성
        UserCharacter character = characterService.createCharacter(
                userId,
                selectedCandidate.getRank(),
                selectedCandidate.getType(),
                selectedCandidate.getCharacterClass(),
                false
        );
        
        // 생성된 캐릭터를 담은 Egg 생성
        Egg egg = new Egg(eggProb.getRank(), eggWalk, 0, obtainedPosition, obtainedDate, false, character, member);
        eggRepository.save(egg);
        return egg;
    }

    // 후보 배열에서 characterClassRandom 값에 따라 캐릭터 후보 선택 method
    // input : candidates(캐릭터 후보 배열), randomVal(캐릭터 선택 랜덤 값)
    private CharacterProbability selectCandidate(CharacterProbability[] candidates, double randomVal) {
        for (CharacterProbability candidate : candidates) {
            if (randomVal <= candidate.getProbability()) {
                return candidate;
            }
        }
        return candidates[candidates.length - 1];
    }

    // 알 상세정보 조회 method
    // input : egg ID
    // output : GetEggDetailResponse
    public GetEggDetailResponse getEggDetail(long eggId) {
        Egg egg = eggRepository.findById(eggId).orElse(null);
        if (egg == null) {
            throw new CustomException(ErrorCode.EGG_NOT_FOUND);
        }

        GetEggDetailResponse response = GetEggDetailResponse.builder()
                .rank(egg.getRank())
                .needStep(egg.getNeedStep())
                .nowStep(egg.getNowStep())
                .obtainedPosition(egg.getObtainedPosition())
                .obtainedDate(egg.getObtainedDate())
                .build();
        return response;
    }

    // 보유한 알 갯수 조회 method
    // input : user ID
    // output : GetEggCountResponse
    public GetEggCountResponse getEggCount(long userId) {
        GetEggCountResponse response = GetEggCountResponse.builder().eggCount(eggRepository.countAllByUserId(userId)).build();
        return response;
    }

    // 알의 걸은 걸음수 업데이트 method
    // input : egg ID, now step
    // output : PatchEggResponse
    public EggResponse updateEggNowStep(long eggId, int nowStep, double latitude, double longitude) {
        Egg egg = eggRepository.findById(eggId).orElse(null);
        if (egg == null) {
            throw new CustomException(ErrorCode.EGG_NOT_FOUND);
        }

        if(egg.getNeedStep() <= nowStep){
            String sido = tmapAPIService.convertGeoToString(latitude,longitude);
            characterService.createCharacterBorn(egg.getUserCharacter().getId(),LocalDate.now(),sido);
            eggRepository.delete(egg);
            EggResponse response = EggResponse.builder()
                    .eggId(egg.getId())
                    .rank(egg.getRank())
                    .nowStep(egg.getNowStep())
                    .needStep(egg.getNeedStep())
                    .userCharacterId(egg.getUserCharacter().getId())
                    .obtainedDate(egg.getObtainedDate())
                    .obtainedPosition(egg.getObtainedPosition())
                    .memberId(egg.getUser().getId())
                    .picked(egg.getPicked())
                    .build();
            return response;
        }

        egg.eggNowStepUpdate(nowStep);
        egg = eggRepository.save(egg);

        EggResponse response = EggResponse.builder()
                .eggId(egg.getId())
                .rank(egg.getRank())
                .nowStep(egg.getNowStep())
                .needStep(egg.getNeedStep())
                .userCharacterId(egg.getUserCharacter().getId())
                .obtainedDate(egg.getObtainedDate())
                .obtainedPosition(egg.getObtainedPosition())
                .memberId(egg.getUser().getId())
                .picked(egg.getPicked())
                .build();
        return response;
    }
}
