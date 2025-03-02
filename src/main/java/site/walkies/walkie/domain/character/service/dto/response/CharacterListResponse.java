package site.walkies.walkie.domain.character.service.dto.response;

import java.util.List;

public class CharacterListResponse {
    private List<GetCharacterResponse> characters;

    public CharacterListResponse(List<GetCharacterResponse> characters) {
        this.characters = characters;
    }

    public List<GetCharacterResponse> getCharacters() {
        return characters;
    }

    public void setCharacters(List<GetCharacterResponse> characters) {
        this.characters = characters;
    }
}
