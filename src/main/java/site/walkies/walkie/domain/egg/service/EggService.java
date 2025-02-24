package site.walkies.walkie.domain.egg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.character.entity.UserCharacter;
import site.walkies.walkie.domain.character.repository.UserCharacterRepository;
import site.walkies.walkie.domain.character.service.CharacterService;
import site.walkies.walkie.domain.egg.entity.Egg;
import site.walkies.walkie.domain.egg.repository.EggRepository;
import site.walkies.walkie.domain.egg.service.dto.response.GetEggResponse;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.global.probability.CharacterProbability;
import site.walkies.walkie.global.probability.EggsProbability;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EggService {

    private final EggRepository eggRepository;
    private final MemberRepository memberRepository;

    private final CharacterService characterService;

    // 보유한 알 리스트 조회 method
    // input : user ID
    // output : List<getEggListResponse>
    public List<GetEggResponse> getEggsList(long userId) {
        List<GetEggResponse> eggs = new ArrayList<>();

        for (Egg egg : eggRepository.findAllByUserId(userId)) {
            GetEggResponse getEggResponse = GetEggResponse.createGetEggResponse(egg.getId(),egg.getRank(),egg.getNeedStep(),egg.getNowStep(),egg.getUserCharacter().getId(),egg.getPicked());
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
            return null;
        }

        // 1. 랜덤값 생성
        double eggRandom = Math.random() * 100;
        double characterRandom = Math.random() * 100;
        double characterClassRandom = Math.random() * 100;

        // 2. Egg 생성 : 여기서는 NORMAL_EGG만 처리한다고 가정
        if (eggRandom <= EggsProbability.NORMAL_EGG.getProbability()) {
            // [일반 캐릭터] - 등급 0
            if (characterRandom <= EggsProbability.NORMAL_EGG.getNormalProbability()) {
                // normal 캐릭터 (rank 0)
                if (characterClassRandom <= CharacterProbability.JELLYFISH.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.JELLYFISH.getRank(),
                            CharacterProbability.JELLYFISH.getType(),
                            CharacterProbability.JELLYFISH.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 2000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else if (characterClassRandom <= CharacterProbability.RED_JELLYFISH.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.RED_JELLYFISH.getRank(),
                            CharacterProbability.RED_JELLYFISH.getType(),
                            CharacterProbability.RED_JELLYFISH.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 2000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else if (characterClassRandom <= CharacterProbability.GREEN_JELLYFISH.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.GREEN_JELLYFISH.getRank(),
                            CharacterProbability.GREEN_JELLYFISH.getType(),
                            CharacterProbability.GREEN_JELLYFISH.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 2000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else if (characterClassRandom <= CharacterProbability.PURPLE_JELLYFISH.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.PURPLE_JELLYFISH.getRank(),
                            CharacterProbability.PURPLE_JELLYFISH.getType(),
                            CharacterProbability.PURPLE_JELLYFISH.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 2000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else if (characterClassRandom <= CharacterProbability.PINK_JELLYFISH.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.PINK_JELLYFISH.getRank(),
                            CharacterProbability.PINK_JELLYFISH.getType(),
                            CharacterProbability.PINK_JELLYFISH.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 2000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else if (characterClassRandom <= CharacterProbability.DINO.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.DINO.getRank(),
                            CharacterProbability.DINO.getType(),
                            CharacterProbability.DINO.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 2000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else if (characterClassRandom <= CharacterProbability.RED_DINO.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.RED_DINO.getRank(),
                            CharacterProbability.RED_DINO.getType(),
                            CharacterProbability.RED_DINO.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 2000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else if (characterClassRandom <= CharacterProbability.GREEN_DINO.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.GREEN_DINO.getRank(),
                            CharacterProbability.GREEN_DINO.getType(),
                            CharacterProbability.GREEN_DINO.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 2000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else if (characterClassRandom <= CharacterProbability.PURPLE_DINO.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.PURPLE_DINO.getRank(),
                            CharacterProbability.PURPLE_DINO.getType(),
                            CharacterProbability.PURPLE_DINO.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 2000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.PINK_DINO.getRank(),
                            CharacterProbability.PINK_DINO.getType(),
                            CharacterProbability.PINK_DINO.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 2000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                }
            }
            // [레어 캐릭터] - 등급 1
            else if (characterRandom <= EggsProbability.NORMAL_EGG.getRareProbability()) {
                if (characterClassRandom <= CharacterProbability.RABBIT_JELLYFISH.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.RABBIT_JELLYFISH.getRank(),
                            CharacterProbability.RABBIT_JELLYFISH.getType(),
                            CharacterProbability.RABBIT_JELLYFISH.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 6000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else if (characterClassRandom <= CharacterProbability.STARFISH_JELLYFISH.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.STARFISH_JELLYFISH.getRank(),
                            CharacterProbability.STARFISH_JELLYFISH.getType(),
                            CharacterProbability.STARFISH_JELLYFISH.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 6000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else if (characterClassRandom <= CharacterProbability.DEER_DINO.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.DEER_DINO.getRank(),
                            CharacterProbability.DEER_DINO.getType(),
                            CharacterProbability.DEER_DINO.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 6000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.NESSIE_DINO.getRank(),
                            CharacterProbability.NESSIE_DINO.getType(),
                            CharacterProbability.NESSIE_DINO.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 6000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                }
            }
            // [에픽 캐릭터] - 등급 2
            else if (characterRandom <= EggsProbability.NORMAL_EGG.getEpicProbability()) {
                if (characterClassRandom <= CharacterProbability.LIGHTNING_JELLYFISH.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.LIGHTNING_JELLYFISH.getRank(),
                            CharacterProbability.LIGHTNING_JELLYFISH.getType(),
                            CharacterProbability.LIGHTNING_JELLYFISH.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 8000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else if (characterClassRandom <= CharacterProbability.STRAWBERRY_JELLYFISH.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.STRAWBERRY_JELLYFISH.getRank(),
                            CharacterProbability.STRAWBERRY_JELLYFISH.getType(),
                            CharacterProbability.STRAWBERRY_JELLYFISH.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 8000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else if (characterClassRandom <= CharacterProbability.PANCAKE_DINO.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.PANCAKE_DINO.getRank(),
                            CharacterProbability.PANCAKE_DINO.getType(),
                            CharacterProbability.PANCAKE_DINO.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 8000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.MELONSODA_DINO.getRank(),
                            CharacterProbability.MELONSODA_DINO.getType(),
                            CharacterProbability.MELONSODA_DINO.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 8000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                }
            }
            // [레전드 캐릭터] - 등급 3
            else if (characterRandom <= EggsProbability.NORMAL_EGG.getLegendaryProbability()) {
                if (characterClassRandom <= CharacterProbability.SPACE_JELLYFISH.getProbability()) {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.SPACE_JELLYFISH.getRank(),
                            CharacterProbability.SPACE_JELLYFISH.getType(),
                            CharacterProbability.SPACE_JELLYFISH.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 10000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                } else {
                    UserCharacter character = characterService.createCharacter(
                            userId,
                            CharacterProbability.DRAGON_DINO.getRank(),
                            CharacterProbability.DRAGON_DINO.getType(),
                            CharacterProbability.DRAGON_DINO.getCharacterClass(),
                            false
                    );
                    Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 10000, 0, obtainedPosition, obtainedDate, false, character, member);
                    return egg;
                }
            }
        }
        return null;
    }

}
