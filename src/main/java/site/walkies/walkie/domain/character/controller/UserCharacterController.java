package site.walkies.walkie.domain.character.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.character.service.CharacterService;
import site.walkies.walkie.domain.character.service.dto.response.CharacterListResponse;
import site.walkies.walkie.domain.character.service.dto.response.GetCharacterCount;
import site.walkies.walkie.domain.character.service.dto.response.GetCharacterDetailResponse;
import site.walkies.walkie.domain.character.service.dto.response.GetCharacterResponse;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.global.auth.dto.MemberPrincipal;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class UserCharacterController {
    private final CharacterService characterService;

    // 보유한 캐릭터 리스트 조회 API
    @GetMapping
    public SuccessResponse<CharacterListResponse> getCharacters(@RequestParam(required = false) Integer type, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        List<GetCharacterResponse> responses = characterService.getCharacters(memberPrincipal.getMemberId(),type);

        CharacterListResponse response = new CharacterListResponse(responses);

        return SuccessResponse.ok(response);
    }

    // 보유한 캐릭터 갯수 조회 API
    @GetMapping("/count")
    public SuccessResponse<GetCharacterCount> getCharacterCount(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        GetCharacterCount response = characterService.getCharacterCount(memberPrincipal.getMemberId());
        return SuccessResponse.ok(response);
    }

    // 캐릭터 획득 정보 상세 조회 API
    @GetMapping("/details/{characterId}")
    public SuccessResponse<GetCharacterDetailResponse> getCharacterDetailResponseSuccessResponse(@PathVariable Long characterId) {
        GetCharacterDetailResponse response = characterService.getCharacterDetailResponse(characterId);
        return SuccessResponse.ok(response);
    }
}
