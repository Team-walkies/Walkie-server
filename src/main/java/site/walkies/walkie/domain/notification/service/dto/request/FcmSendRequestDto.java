package site.walkies.walkie.domain.notification.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FcmSendRequestDto {
    @NotBlank(message = "FCM 토큰은 필수입니다.")
    private String token;
    private String title;
    private String body;
}
