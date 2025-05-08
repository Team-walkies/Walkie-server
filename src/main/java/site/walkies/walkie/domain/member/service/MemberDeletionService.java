package site.walkies.walkie.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.walkies.walkie.domain.character.service.CharacterService;
import site.walkies.walkie.domain.egg.service.EggService;
import site.walkies.walkie.domain.member.entity.Member;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.domain.member.token.MemberRefreshTokenService;
import site.walkies.walkie.domain.review.service.ReviewService;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberDeletionService {

    // 여러 개의 서비스나 리포를 한꺼번에 의존성을 받아서 작업을 해야 하기 때문에, 기존 memberService와 분리했습니다.

    private final MemberRepository memberRepository;
    private final ReviewService reviewService;
    private final EggService eggService;
    private final CharacterService characterService;
    private final MemberRefreshTokenService refreshTokenService;

    // 회원 탈퇴 요청 시 (Soft Delete)
    @Transactional
    public Long softDeleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        member.markDeleted(LocalDateTime.now()); // deleteCd = true, deleteRequestedAt 설정
        member.changeLevelingEgg(null);
        member.changeLevelingUserCharacter(null);

        reviewService.deleteReviewCodeByUserId(memberId);
        refreshTokenService.deleteByMember(member);

        return member.getId();
    }

    // 한 달 뒤 회원 완전 삭제 (스케줄러 또는 관리용 배치에서 호출)
    @Transactional
    public void deleteOutdatedMembers() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        List<Member> members = memberRepository.findAllByDeleteCdTrueAndDeleteRequestedAtBefore(threshold);

        for (Member member : members) {
            Long memberId = member.getId();

            reviewService.deleteReviewByUserId(memberId);
            member.changeLevelingEgg(null);
            member.changeLevelingUserCharacter(null);
            eggService.deleteEggByUserId(memberId);
            characterService.deleteEggByUserId(memberId);
            memberRepository.delete(member);
        }
    }
}