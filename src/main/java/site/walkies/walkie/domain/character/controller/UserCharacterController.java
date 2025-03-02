package site.walkies.walkie.domain.character.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.walkies.walkie.domain.character.service.CharacterService;
import site.walkies.walkie.domain.character.service.dto.response.CharacterListResponse;
import site.walkies.walkie.domain.character.service.dto.response.GetCharacterCount;
import site.walkies.walkie.domain.character.service.dto.response.GetCharacterResponse;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class UserCharacterController {
    private final CharacterService characterService;

    @GetMapping
    public SuccessResponse<CharacterListResponse> getCharacters(@RequestParam(required = false) Integer type) {
        List<GetCharacterResponse> responses = characterService.getCharacters(2,type);

        CharacterListResponse response = new CharacterListResponse(responses);

        return SuccessResponse.ok(response);
    }

    // 보유한 캐릭터 갯수 조회 API
    @GetMapping("/count")
    public SuccessResponse<GetCharacterCount> getCharacterCount() {
        GetCharacterCount response = characterService.getCharacterCount(2);

        return SuccessResponse.ok(response);
    }
}
