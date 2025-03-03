package site.walkies.walkie.domain.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.notice.entity.Notice;
import site.walkies.walkie.domain.notice.repository.NoticeRepository;
import site.walkies.walkie.domain.notice.service.dto.request.PostNoticeRequest;
import site.walkies.walkie.domain.notice.service.dto.response.PostNoticeResponse;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    // 공지사항 등록 method
    // input : date(등록일), title(제목), detail(내용)
    // output : PostNoticeResponse
    public PostNoticeResponse postNotice(LocalDate date, String title, String detail) {
        Notice notice = Notice.createNotice(date, title,detail);
        Notice postNotice = noticeRepository.save(notice);
        PostNoticeResponse response = PostNoticeResponse.builder()
                .id(postNotice.getId())
                .title(postNotice.getTitle())
                .detail(postNotice.getDetail())
                .date(postNotice.getNoticeDate())
                .build();

        return response;
    }
}
