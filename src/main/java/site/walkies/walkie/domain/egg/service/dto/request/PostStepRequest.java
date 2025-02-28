package site.walkies.walkie.domain.egg.service.dto.request;

import lombok.Getter;

@Getter
public class PostStepRequest {
    private Long eggId;
    private Integer nowStep;
    private Double latitude;
    private Double longitude;
}
