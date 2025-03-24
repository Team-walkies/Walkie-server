package site.walkies.walkie.domain.character.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(
            summary = "보유 캐릭터 목록 조회",
            description = "사용자가 보유 중인 캐릭터 목록을 조회합니다. " +
                    "선택적으로 캐릭터 타입(type)을 쿼리 파라미터로 지정하여 필터링할 수 있습니다."
    )
    // 보유한 캐릭터 리스트 조회 API
    @GetMapping
    public SuccessResponse<CharacterListResponse> getCharacters(
            @Parameter(
                    description = "캐릭터 타입 (0: 해파리, 1: 공룡)",
                    required = false
            )
            @RequestParam(required = false) Integer type,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        List<GetCharacterResponse> responses = characterService.getCharacters(memberPrincipal.getMemberId(),type);

        CharacterListResponse response = new CharacterListResponse(responses);

        return SuccessResponse.ok(response);
    }

    @Operation(
            summary = "보유 캐릭터 수 조회",
            description = "사용자가 보유 중인 캐릭터의 총 개수를 조회합니다."
    )
    // 보유한 캐릭터 갯수 조회 API
    @GetMapping("/count")
    public SuccessResponse<GetCharacterCount> getCharacterCount(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        GetCharacterCount response = characterService.getCharacterCount(memberPrincipal.getMemberId());
        return SuccessResponse.ok(response);
    }


    @Operation(
            summary = "캐릭터 획득 정보 조회",
            description = "특정 캐릭터의 상세 획득 정보를 조회합니다. " +
                    "캐릭터 ID를 경로 변수로 전달해야 합니다."
    )
    // 캐릭터 획득 정보 상세 조회 API
    @GetMapping("/details/{characterId}")
    public SuccessResponse<GetCharacterDetailResponse> getCharacterDetailResponseSuccessResponse(@PathVariable Long characterId) {
        GetCharacterDetailResponse response = characterService.getCharacterDetailResponse(characterId);
        return SuccessResponse.ok(response);
    }
}
