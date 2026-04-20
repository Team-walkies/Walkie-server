// DailyEggEventService.java
package site.walkies.walkie.domain.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.walkies.walkie.domain.egg.service.EggService;
import site.walkies.walkie.domain.event.entity.DailyEggEvent;
import site.walkies.walkie.domain.event.repository.DailyEggEventRepository;
import site.walkies.walkie.domain.event.service.dto.DailyEggEventResponse;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyEggEventService {

    private final MemberRepository memberRepository;
    private final DailyEggEventRepository eventRepository;
    private final EggService eggService;

    private static final LocalDate EVENT_START = LocalDate.of(2025, Month.JULY, 7);
    private static final LocalDate EVENT_END = LocalDate.of(2025, Month.JULY, 31);

    @Transactional
    public DailyEggEventResponse provideDailyEgg(Long memberId) {
        LocalDate today = LocalDate.now();

        int remainingDays = (int) ChronoUnit.DAYS.between(today, EVENT_END);
        remainingDays = Math.max(remainingDays, 0); // 음수 방지 (8월 이후는 0)

        if (today.isBefore(EVENT_START) || today.isAfter(EVENT_END)) {
            // 이벤트 기간이 아닐 경우 CustomException을 던지도록 수정
            throw new CustomException(ErrorCode.EVENT_PERIOD_ENDED);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Optional<DailyEggEvent> optionalEvent = eventRepository.findByMember(member);

        if (optionalEvent.isEmpty()) {
            // 첫 참여
            eggService.createEgg(memberId, -1, -1);
            DailyEggEvent newEvent = new DailyEggEvent(member, today);
            eventRepository.save(newEvent);
            return new DailyEggEventResponse(true, remainingDays);
        }

        DailyEggEvent event = optionalEvent.get();
        if (today.equals(event.getLastReceivedDate())) {
            return new DailyEggEventResponse(false, remainingDays);
        }

        // 아직 오늘 알 안 받음
        eggService.createEgg(memberId, -1, -1);
        event.updateLastReceivedDate(today);
        return new DailyEggEventResponse(true, remainingDays);
    }
}
