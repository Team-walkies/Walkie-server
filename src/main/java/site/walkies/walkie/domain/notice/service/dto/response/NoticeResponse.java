package site.walkies.walkie.domain.notice.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class NoticeResponse {
    private long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String title;
    private String detail;
}
