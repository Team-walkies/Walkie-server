package site.walkies.walkie.domain.notification.service.dto.request;

import lombok.Getter;

@Getter
public class FcmSendRequestDto {
    private String token;
    private String title;
    private String body;
}
