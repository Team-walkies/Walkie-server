package site.walkies.walkie.domain.notice.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PostNoticeResponse {
    private long id;
    private LocalDate date;
    private String title;
    private String detail;
}
