package site.walkies.walkie.domain.notice.service.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostNoticeRequest {
    private LocalDate date;
    private String title;
    private String detail;
}
