package site.walkies.walkie.domain.health.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.health.entity.HealthCurrent;
import site.walkies.walkie.domain.health.entity.HealthHistory;
import site.walkies.walkie.domain.health.enums.Calorie;
import site.walkies.walkie.domain.health.repository.HealthCurrentRepository;
import site.walkies.walkie.domain.health.repository.HealthHistoryRepository;
import site.walkies.walkie.domain.health.service.dto.response.HealthContinueDayResponseDto;
import site.walkies.walkie.domain.health.service.dto.response.HealthDetailResponseDto;
import site.walkies.walkie.domain.health.service.dto.response.HealthMoveResponseDto;
import site.walkies.walkie.domain.health.service.dto.response.HealthResponseDto;
import site.walkies.walkie.domain.member.repository.MemberRepository;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthService {
    private final HealthCurrentRepository healthCurrentRepository;
    private final HealthHistoryRepository healthHistoryRepository;
    private final MemberRepository memberRepository;

    // 상세 조회 method
    public HealthDetailResponseDto getHealthDetail(long memberId, LocalDate searchDate) {
        // 오늘 이후의 날짜인 경우 exception 발생
        if(searchDate.isAfter(LocalDate.now())) {
            throw new CustomException(ErrorCode.DATE_OVER_ERROR);
        }

        // 상세 조회 검색 (current DB)
        HealthCurrent healthCurrent = healthCurrentRepository.findByMemberIdAndNowDay(memberId,searchDate).orElse(null);

        if(healthCurrent != null) {
            return healthDetailResponseDtoBuilder(healthCurrent.getTargetSteps(), healthCurrent.getNowSteps(), healthCurrent.getNowDistance(), healthCurrent.getNowCalories());
        }

        // 상세 조회 검색 (history DB)
        HealthHistory healthHistory = healthHistoryRepository.findByMemberIdAndRecordDate(memberId, searchDate).orElse(null);

        if(healthHistory != null) {
            return healthDetailResponseDtoBuilder(healthHistory.getTargetSteps(), healthHistory.getDaySteps(), healthHistory.getDayDistance(), healthHistory.getDayCalories());
        }

        // 둘 다 null 인 경우 기본 값 노출
        return healthDetailResponseDtoBuilder(getCurrentTargetSteps(memberId,searchDate), 0, 0.0,0.0);
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
    private HealthDetailResponseDto healthDetailResponseDtoBuilder(int targetSteps, int steps, double distance, double calories) {
        Calorie targetCal = calculateCalories(calories);

        return HealthDetailResponseDto.builder()
                .targetSteps(targetSteps)
                .nowSteps(steps)
                .nowDistance(distance)
                .nowCalories(calories)
                .caloriesName(targetCal.getFoodName())
                .caloriesDescription(targetCal.getFoodDescription())
                .caloriesUrl("https://truthguard.site/api/v1/file/" + targetCal.getImageUrl() + ".png")
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

            if(healthCurrent != null) {
                healthResponseDtoList.add(HealthResponseDto.builder()
                        .responseDate(tempDate)
                        .targetSteps(healthCurrent.getTargetSteps())
                        .nowSteps(healthCurrent.getNowSteps())
                        .build());
                continue;
            }

            // history에서 조회
            HealthHistory healthHistory = healthHistoryRepository.findByMemberIdAndRecordDate(memberId, tempDate).orElse(null);

            if(healthHistory != null) {
                healthResponseDtoList.add(HealthResponseDto.builder()
                        .responseDate(tempDate)
                        .targetSteps(healthHistory.getTargetSteps())
                        .nowSteps(healthHistory.getDaySteps())
                        .build());
                continue;
            }

            // 둘 다 없는 경우 기본값 세팅
            healthResponseDtoList.add(HealthResponseDto.builder()
                    .responseDate(tempDate)
                    .targetSteps(getCurrentTargetSteps(memberId,tempDate))
                    .nowSteps(0)
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

    public void updateHealthDB (long memberId) {
        // 어제 날짜
        LocalDate yesterday = LocalDate.now().minusDays(1);

        HealthCurrent healthCurrent = healthCurrentRepository.findByMemberIdAndNowDay(memberId,yesterday).orElse(null);
        HealthHistory oneDaysAgoHistory = healthHistoryRepository.findByMemberIdAndRecordDate(memberId, yesterday).orElse(null);
        // 오늘의 데이터가 초기화 되지 않고 어제 데이터가 안들어가있는 경우
        if(healthCurrent != null && oneDaysAgoHistory == null) {
            // 연속일 수 계산
            int continueDay = 0;
            HealthHistory twoDaysAgoHistory = healthHistoryRepository.findByMemberIdAndRecordDate(memberId,yesterday.minusDays(1)).orElse(null);
            if(healthCurrent.getTargetSteps() <= healthCurrent.getNowSteps()) {
                if(twoDaysAgoHistory == null) {
                    continueDay = 1;
                } else {
                    continueDay = twoDaysAgoHistory.getContinuousDays() + 1;
                }
            }


            oneDaysAgoHistory = HealthHistory.create(healthCurrent.getMember(),
                    yesterday,
                    healthCurrent.getTargetSteps(),
                    healthCurrent.getNowSteps(),
                    healthCurrent.getNowDistance(),
                    healthCurrent.getNowCalories(),
                    continueDay
                    );

            healthHistoryRepository.save(oneDaysAgoHistory);
            healthCurrentRepository.delete(healthCurrent);
        }
    }

    // 최근 target step 구하기
    private int getCurrentTargetSteps(long memberId, LocalDate nowDate) {
        HealthCurrent healthCurrent = healthCurrentRepository.findLatestBeforeDate(memberId,nowDate).orElse(null);
        if(healthCurrent != null) {
            return healthCurrent.getTargetSteps();
        }

        HealthHistory healthHistory = healthHistoryRepository.findLatestBeforeDate(memberId,nowDate).orElse(null);
        if(healthHistory != null) {
            return healthHistory.getTargetSteps();
        }
        return 6000;
    }
}
