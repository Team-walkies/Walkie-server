package site.walkies.walkie.domain.health.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.health.entity.HealthCurrent;
import site.walkies.walkie.domain.health.entity.HealthHistory;
import site.walkies.walkie.domain.health.enums.Calorie;
import site.walkies.walkie.domain.health.repository.HealthCurrentRepository;
import site.walkies.walkie.domain.health.repository.HealthHistoryRepository;
import site.walkies.walkie.domain.health.service.dto.response.HealthDetailResponseDto;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class HealthService {
    private final HealthCurrentRepository healthCurrentRepository;
    private final HealthHistoryRepository healthHistoryRepository;

    // 상세 조회 method
    public HealthDetailResponseDto getHealthDetail(long memberId, LocalDate searchDate) {
        // 상세 조회가 오늘 보다 전인 경우 : history DB
        if (searchDate.isBefore(LocalDate.now())) {
            // 상세 조회 검색
            HealthHistory healthHistory = healthHistoryRepository.findByMemberIdAndRecordDate(memberId, searchDate).orElse(null);
            // 조회 데이터가 없는 경우
            if (healthHistory == null) {
                // 기본 값 노출
                return healthDetailResponseDtoBuilder(6000, 0, 0.0,0.0);
            } else {
                return healthDetailResponseDtoBuilder(healthHistory.getTargetSteps(), healthHistory.getDaySteps(), healthHistory.getDayDistance(), healthHistory.getDayCalories());
            }
        }

        // 상세 조회가 오늘인 경우 : cuurent DB
        else if (searchDate.isEqual(LocalDate.now())) {
            // 상세 조회 검색
            HealthCurrent healthCurrent = healthCurrentRepository.findByMemberId(memberId).orElse(null);

            // 상세 조회시 오늘 저장된 데이터가 아직 없는 경우
            if (healthCurrent == null) {
                // 기본 값 노출
                return healthDetailResponseDtoBuilder(6000, 0, 0.0,0.0);
            } else {
                return healthDetailResponseDtoBuilder(healthCurrent.getTargetSteps(), healthCurrent.getNowSteps(), healthCurrent.getNowDistance(), healthCurrent.getNowCalories());
            }
        }

        // exception 발생
        else {
            throw new CustomException(ErrorCode.DATE_OVER_ERROR);
        }
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

}
