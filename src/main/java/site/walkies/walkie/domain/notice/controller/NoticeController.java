package site.walkies.walkie.domain.notice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.walkies.walkie.domain.notice.service.NoticeService;
import site.walkies.walkie.domain.notice.service.dto.request.PostNoticeRequest;
import site.walkies.walkie.domain.notice.service.dto.response.PostNoticeResponse;
import site.walkies.walkie.global.web.dto.response.SuccessResponse;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping
    public SuccessResponse<PostNoticeResponse> postNotice(@RequestBody PostNoticeRequest request) {
        PostNoticeResponse response = noticeService.postNotice(request.getDate(),request.getTitle(),request.getDetail());
        return SuccessResponse.created(response);
    }
}
