package site.walkies.walkie.domain.notice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.domain.notice.service.NoticeService;
import site.walkies.walkie.domain.notice.service.dto.request.PostNoticeRequest;
import site.walkies.walkie.domain.notice.service.dto.response.GetListNoticeResponse;
import site.walkies.walkie.domain.notice.service.dto.response.NoticeResponse;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping
    public SuccessResponse<NoticeResponse> postNotice(@RequestBody PostNoticeRequest request) {
        NoticeResponse response = noticeService.postNotice(request.getDate(),request.getTitle(),request.getDetail());
        return SuccessResponse.created(response);
    }

    @GetMapping
    public SuccessResponse<GetListNoticeResponse> getListNotice() {
        GetListNoticeResponse response = noticeService.getListNotice();
        return SuccessResponse.ok(response);
    }
}
