package site.walkies.walkie.domain.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "공지사항 등록",
            description = "새로운 공지사항을 등록합니다. 요청 본문에는 공지 날짜, 제목, 내용이 포함되어야 합니다."
    )
    @PostMapping
    public SuccessResponse<NoticeResponse> postNotice(@RequestBody PostNoticeRequest request) {
        NoticeResponse response = noticeService.postNotice(request.getDate(),request.getTitle(),request.getDetail());
        return SuccessResponse.created(response);
    }

    @Operation(
            summary = "공지사항 목록 조회",
            description = "등록된 모든 공지사항을 조회합니다."
    )
    @GetMapping
    public SuccessResponse<GetListNoticeResponse> getListNotice() {
        GetListNoticeResponse response = noticeService.getListNotice();
        return SuccessResponse.ok(response);
    }
}
