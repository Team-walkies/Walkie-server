package site.walkies.walkie.domain.member.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.walkies.walkie.domain.member.service.MemberDeletionService;

@Component
@RequiredArgsConstructor
public class MemberCleanupScheduler {

    private final MemberDeletionService memberDeletionService;


    //  매일 새벽 3시 정각에 삭제 유예 기간이 지난 회원을 완전 삭제
    @Scheduled(cron = "0 0 3 * * ?") // 매일 03:00 AM
    public void cleanUpDeletedMembers() {
        memberDeletionService.deleteOutdatedMembers();
    }
}
