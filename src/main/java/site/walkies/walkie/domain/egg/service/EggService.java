package site.walkies.walkie.domain.egg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.egg.entity.Egg;
import site.walkies.walkie.domain.egg.repository.EggRepository;
import site.walkies.walkie.domain.egg.service.dto.response.GetEggResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EggService {

    private final EggRepository repository;

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
}
