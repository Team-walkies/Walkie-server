package site.walkies.walkie.domain.egg.service.dto.response;

import java.util.List;

public class EggListResponse {
    private List<GetEggResponse> eggs;

    public EggListResponse(List<GetEggResponse> eggs) {
        this.eggs = eggs;
    }

    public List<GetEggResponse> getEggs() {
        return eggs;
    }

    public void setEggs(List<GetEggResponse> eggs) {
        this.eggs = eggs;
    }
}