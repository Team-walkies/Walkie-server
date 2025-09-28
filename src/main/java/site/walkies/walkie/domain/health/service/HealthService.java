package site.walkies.walkie.domain.health.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.egg.entity.HealthAwardRecord;
import site.walkies.walkie.domain.egg.repository.HealthAwardRecordRepository;
import site.walkies.walkie.domain.health.entity.HealthCurrent;
import site.walkies.walkie.domain.health.entity.HealthHistory;
import site.walkies.walkie.domain.health.enums.Calorie;
import site.walkies.walkie.domain.health.repository.HealthCurrentRepository;
import site.walkies.walkie.domain.health.repository.HealthHistoryRepository;
import site.walkies.walkie.domain.health.service.dto.response.*;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HealthService {
    private final HealthCurrentRepository healthCurrentRepository;
    private final HealthHistoryRepository healthHistoryRepository;
    private final HealthAwardRecordRepository healthAwardRecordRepository;
    private final MemberRepository memberRepository;

    // 상세 조회 method
    public HealthDetailResponseDto getHealthDetail(long memberId, LocalDate searchDate) {
        // 오늘 이후의 날짜인 경우 exception 발생
        if(searchDate.isAfter(LocalDate.now())) {
            throw new CustomException(ErrorCode.DATE_OVER_ERROR);
        }

        // 상세 조회 검색 (current DB)
        HealthCurrent healthCurrent = healthCurrentRepository.findByMemberIdAndNowDay(memberId,searchDate).orElse(null);

        boolean award = false;

        if(healthCurrent != null) {
            award = awardReceivedCheck(healthCurrent.getNowSteps(), healthCurrent.getTargetSteps(), memberId, searchDate);
            return healthDetailResponseDtoBuilder(healthCurrent.getTargetSteps(), healthCurrent.getNowSteps(), healthCurrent.getNowDistance(), healthCurrent.getNowCalories(),award);
        }

        // 상세 조회 검색 (history DB)
        HealthHistory healthHistory = healthHistoryRepository.findByMemberIdAndRecordDate(memberId, searchDate).orElse(null);

        if(healthHistory != null) {
            award = awardReceivedCheck(healthHistory.getDaySteps(), healthHistory.getTargetSteps(), memberId, searchDate);
            return healthDetailResponseDtoBuilder(healthHistory.getTargetSteps(), healthHistory.getDaySteps(), healthHistory.getDayDistance(), healthHistory.getDayCalories(),award);
        }

        // 둘 다 null 인 경우 기본 값 노출
        return healthDetailResponseDtoBuilder(getCurrentTargetSteps(memberId,searchDate), 0, 0.0,0.0, award);
    }

    // 칼로리 enum 검색 method
    private Calorie calculateCalories(double calories) {
        Calorie targetCal = Calorie.AIR;
        for (Calorie calorie: Calorie.values()) {
            if(calorie.getCalories() <= calories) {
                targetCal = calorie;
                continue;
            }
            break;
        }
        return targetCal;
    }

    // response 생성 method
    private HealthDetailResponseDto healthDetailResponseDtoBuilder(int targetSteps, int steps, double distance, double calories,boolean award) {
        Calorie targetCal = calculateCalories(calories);

        return HealthDetailResponseDto.builder()
                .targetSteps(targetSteps)
                .nowSteps(steps)
                .nowDistance(distance)
                .nowCalories(calories)
                .caloriesName(targetCal.getFoodName())
                .caloriesDescription(targetCal.getFoodDescription())
                .caloriesUrl("https://truthguard.site/api/v1/file/" + targetCal.getImageUrl() + ".png")
                .award(award)
                .build();
    }

    // 헬스케어 저장 method
    public HealthMoveResponseDto updateHealthDetail(long memberId, int targetSteps, int nowSteps, double nowDistance, double calories, LocalDate nowDay) {
        // 현재 헬스케어 정보 조회
        HealthCurrent healthCurrent = healthCurrentRepository.findByMemberIdAndNowDay(memberId,nowDay).orElse(null);
        // 없는 경우 생성
        if(healthCurrent == null) {
            healthCurrent = HealthCurrent.create(memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)),targetSteps, nowSteps,nowDistance,calories,nowDay);
        }
        // 있는 경우 업데이트
        else {
            healthCurrent.updateMove(nowSteps,nowDistance,calories);
        }
        // 저장
        healthCurrentRepository.save(healthCurrent);
        return HealthMoveResponseDto.builder()
                .targetSteps(targetSteps)
                .nowSteps(nowSteps)
                .nowDistance(nowDistance)
                .nowCalories(calories)
                .build();
    }

    // 헬스케어 List 조회 method
    public List<HealthResponseDto> getHealthList(long memberId, LocalDate startDate, LocalDate endDate) {
        List<HealthResponseDto> healthResponseDtoList = new ArrayList<>();
        for(LocalDate tempDate = startDate;!tempDate.isAfter(endDate);tempDate = tempDate.plusDays(1) ) {
            // current에서 조회
            HealthCurrent healthCurrent = healthCurrentRepository.findByMemberIdAndNowDay(memberId,tempDate).orElse(null);

            boolean award = false;

            if(healthCurrent != null) {
                award = awardReceivedCheck(healthCurrent.getNowSteps(), healthCurrent.getTargetSteps(), memberId, tempDate);
                healthResponseDtoList.add(HealthResponseDto.builder()
                        .responseDate(tempDate)
                        .targetSteps(healthCurrent.getTargetSteps())
                        .nowSteps(healthCurrent.getNowSteps())
                        .award(award)
                        .build());
                continue;
            }

            // history에서 조회
            HealthHistory healthHistory = healthHistoryRepository.findByMemberIdAndRecordDate(memberId, tempDate).orElse(null);

            if(healthHistory != null) {
                award = awardReceivedCheck(healthHistory.getDaySteps(), healthHistory.getTargetSteps(), memberId, tempDate);
                healthResponseDtoList.add(HealthResponseDto.builder()
                        .responseDate(tempDate)
                        .targetSteps(healthHistory.getTargetSteps())
                        .nowSteps(healthHistory.getDaySteps())
                        .award(award)
                        .build());
                continue;
            }

            // 둘 다 없는 경우 기본값 세팅
            healthResponseDtoList.add(HealthResponseDto.builder()
                    .responseDate(tempDate)
                    .targetSteps(getCurrentTargetSteps(memberId,tempDate))
                    .nowSteps(0)
                    .award(award)
                    .build());
        }
        return healthResponseDtoList;
    }
    
    // 연소일 수 조회 method
    public HealthContinueDayResponseDto getHealthContinueDay(long memberId) {


        HealthHistory healthHistory = healthHistoryRepository.findByMemberIdAndRecordDate(memberId, LocalDate.now().minusDays(1)).orElse(null);

        // 전날 기록이 없는 경우
        if (healthHistory == null) {
            HealthCurrent healthCurrent = healthCurrentRepository.findByMemberIdAndNowDay(memberId, LocalDate.now().minusDays(1)).orElse(null);
            if(healthCurrent != null) {
                if(healthCurrent.getTargetSteps() <= healthCurrent.getNowSteps()) {
                    // 연속일 수 계산
                    int continueDay = 0;
                    HealthHistory twoDaysAgoHistory = healthHistoryRepository.findByMemberIdAndRecordDate(memberId,LocalDate.now().minusDays(2)).orElse(null);
                    if(healthCurrent.getTargetSteps() <= healthCurrent.getNowSteps()) {
                        if(twoDaysAgoHistory == null) {
                            continueDay = 1;
                        } else {
                            continueDay = twoDaysAgoHistory.getContinuousDays() + 1;
                        }
                    }
                    return HealthContinueDayResponseDto.builder()
                            .continuousDays(continueDay)
                            .build();
                }
            }
            return HealthContinueDayResponseDto.builder()
                    .continuousDays(0)
                    .build();
        }

        // 전날 기록이 있는경우
        else {
            return HealthContinueDayResponseDto.builder()
                    .continuousDays(healthHistory.getContinuousDays())
                    .build();
        }
    }

    // 모든 날짜를 업데이트 하도록 변경
    public void updateHealthDB (long memberId) {
        // 해당 회원의 current 전체를 날짜 오름차순으로 가져옴
        List<HealthCurrent> currents =
                healthCurrentRepository.findAllByMemberIdOrderByNowDayAsc(memberId);

        if (currents.isEmpty()) return;

        // 오래된 날짜부터 처리 (연속일 계산이 끊기지 않도록)
        for (HealthCurrent hc : currents) {
            LocalDate date = hc.getNowDay();

            // 연속일 계산: 목표 달성 시에만 +1
            int continueDay = 0;
            boolean achieved = hc.getTargetSteps() != null
                    && hc.getNowSteps() != null
                    && hc.getNowSteps() >= hc.getTargetSteps();

            if (achieved) {
                HealthHistory prev = healthHistoryRepository
                        .findByMemberIdAndRecordDate(memberId, date.minusDays(1))
                        .orElse(null);
                continueDay = (prev == null) ? 1 : prev.getContinuousDays() + 1;
            }

            // 같은 날짜의 history가 있으면 UPDATE, 없으면 CREATE
            Optional<HealthHistory> historyOpt =
                    healthHistoryRepository.findByMemberIdAndRecordDate(memberId, date);

            if (historyOpt.isPresent()) {
                // 업데이트
                HealthHistory history = historyOpt.get();
                history.update(
                        hc.getTargetSteps(),
                        hc.getNowSteps(),
                        hc.getNowDistance(),
                        hc.getNowCalories(),
                        continueDay
                );
            } else {
                // 새로 생성
                HealthHistory history = HealthHistory.create(
                        hc.getMember(),
                        date,
                        hc.getTargetSteps(),
                        hc.getNowSteps(),
                        hc.getNowDistance(),
                        hc.getNowCalories(),
                        continueDay
                );
                healthHistoryRepository.save(history);
            }

            // 이관 후 삭제
            healthCurrentRepository.delete(hc);
        }
    }

    // 최근 target step 구하기
    private int getCurrentTargetSteps(long memberId, LocalDate nowDate) {
        HealthCurrent healthCurrent = healthCurrentRepository.findFirstByMemberIdAndNowDayBeforeOrderByNowDayDesc(memberId,nowDate).orElse(null);
        if(healthCurrent != null) {
            return healthCurrent.getTargetSteps();
        }

        HealthHistory healthHistory = healthHistoryRepository.findFirstByMemberIdAndRecordDateBeforeOrderByRecordDateDesc(memberId,nowDate).orElse(null);
        if(healthHistory != null) {
            return healthHistory.getTargetSteps();
        }
        return 6000;
    }

    // 최근 헬스케어 데이터 날짜 구하기
    public HealthLastDataDayResponseDto getCurrentHealthDay(long memberId) {
        HealthCurrent healthCurrent = healthCurrentRepository.findFirstByMemberIdAndNowDayBeforeOrderByNowDayDesc(memberId,LocalDate.now().plusDays(1)).orElse(null);
        if(healthCurrent != null) {
            return HealthLastDataDayResponseDto.builder()
                    .lastDataDay(healthCurrent.getNowDay())
                    .build();
        }

        HealthHistory healthHistory = healthHistoryRepository.findFirstByMemberIdAndRecordDateBeforeOrderByRecordDateDesc(memberId,LocalDate.now().plusDays(1)).orElse(null);
        if(healthHistory != null) {
            return HealthLastDataDayResponseDto.builder()
                    .lastDataDay(healthHistory.getRecordDate())
                    .build();
        }
        return HealthLastDataDayResponseDto.builder()
                .lastDataDay(LocalDate.of(2025, 7, 31))
                .build();
    }

    // 보상을 받았는지 판단하는 함수
    private boolean awardReceivedCheck(int nowSteps, int targetSteps,long memberId, LocalDate searchDate) {
        // 목표를 달성한 경우
        if(nowSteps >= targetSteps) {
            // 보상을 받았는지 조회
            HealthAwardRecord healthAwardRecord = healthAwardRecordRepository.findByMemberIdAndReceivedDate(memberId, searchDate).orElse(null);
            // 받지 않은 경우 + 회원가입일 보다 이후인 경우(로직 추가 필요)
            if(healthAwardRecord == null) {
                return true;
            }
        }
        return false;
    }
}
