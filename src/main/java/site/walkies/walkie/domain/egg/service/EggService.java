package site.walkies.walkie.domain.egg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.egg.entity.Egg;
import site.walkies.walkie.domain.egg.repository.EggRepository;
import site.walkies.walkie.domain.egg.service.dto.response.GetEggResponse;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.global.enums.EggsProbability;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EggService {

    private final EggRepository repository;
    private final MemberRepository memberRepository;

    // 보유한 알 리스트 조회 method
    // input : user ID
    // output : List<getEggListResponse>
    public List<GetEggResponse> getEggsList(long userId) {
        List<GetEggResponse> eggs = new ArrayList<>();

        for (Egg egg : repository.findAllByUserId(userId)) {
            GetEggResponse getEggResponse = GetEggResponse.createGetEggResponse(egg.getId(),egg.getRank(),egg.getNeedStep(),egg.getNowStep(),egg.getUserCharacter().getId(),egg.getPicked());
            eggs.add(getEggResponse);
        }

        return eggs;
    }

    // egg 생성 method
    // input : userId, obtainedPosition(얻은 위치), obtainedDate(얻은 날짜)
    // output : Egg
    public Egg createEgg (long userId, String obtainedPosition, LocalDate obtainedDate) {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) { return null; }

        // 확률에 따른 알 생성
        // 1.랜덤값 생성
        double random = Math.random() * 100;
        // 2. 범위에 따라 다른 Egg 생성
        if(random <= EggsProbability.NORMAL_EGG.getProbability()) {
            // 3. 확률에 따라 캐릭터 생성

            
            Egg egg = new Egg(EggsProbability.NORMAL_EGG.getRank(), 2000,0, obtainedPosition, obtainedDate, false,null,member);
            return egg;
        }


        return null;
    }
}
