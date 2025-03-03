package site.walkies.walkie.domain.notice.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetListNoticeResponse {
    private List<NoticeResponse> notices;
}
