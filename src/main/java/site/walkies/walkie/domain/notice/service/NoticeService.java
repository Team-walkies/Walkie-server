package site.walkies.walkie.domain.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.notice.entity.Notice;
import site.walkies.walkie.domain.notice.repository.NoticeRepository;
import site.walkies.walkie.domain.notice.service.dto.response.GetListNoticeResponse;
import site.walkies.walkie.domain.notice.service.dto.response.NoticeResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    // 공지사항 등록 method
    // input : date(등록일), title(제목), detail(내용)
    // output : PostNoticeResponse
    public NoticeResponse postNotice(LocalDate date, String title, String detail) {
        Notice notice = Notice.createNotice(date, title,detail);
        Notice postNotice = noticeRepository.save(notice);
        NoticeResponse response = NoticeResponse.builder()
                .id(postNotice.getId())
                .title(postNotice.getTitle())
                .detail(postNotice.getDetail())
                .date(postNotice.getNoticeDate())
                .build();

        return response;
    }

    // 공지사항 리스트 조회 method
    // input : X
    // output : GetListNoticeResponse
    public GetListNoticeResponse getListNotice() {
        List<Notice> noticeList = noticeRepository.findAll();
        List<NoticeResponse> noticeResponseList = new ArrayList<>();

        for(Notice notice : noticeList) {
            NoticeResponse response = NoticeResponse.builder()
                    .id(notice.getId())
                    .title(notice.getTitle())
                    .detail(notice.getDetail())
                    .date(notice.getNoticeDate())
                    .build();
            noticeResponseList.add(response);
        }

        GetListNoticeResponse response = GetListNoticeResponse.builder()
                .notices(noticeResponseList)
                .build();

        return response;

    }
}
