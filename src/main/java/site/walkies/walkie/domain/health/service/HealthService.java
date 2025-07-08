package site.walkies.walkie.domain.health.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.walkies.walkie.domain.health.entity.HealthCurrent;
import site.walkies.walkie.domain.health.entity.HealthHistory;
import site.walkies.walkie.domain.health.enums.Calorie;
import site.walkies.walkie.domain.health.repository.HealthCurrentRepository;
import site.walkies.walkie.domain.health.repository.HealthHistoryRepository;
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

    // 헬스케어 저장 method
    public HealthMoveResponseDto updateHealthDetail(long memberId, int targetSteps, int nowSteps, double nowDistance, double calories) {
        // 현재 헬스케어 정보 조회
        HealthCurrent healthCurrent = healthCurrentRepository.findByMemberId(memberId).orElse(null);
        // 없는 경우 생성
        if(healthCurrent == null) {
            healthCurrent = HealthCurrent.create(memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)),targetSteps, nowSteps,nowDistance,calories);
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
            // 오늘인 경우
            if(tempDate.isEqual(LocalDate.now())) {
                HealthCurrent healthCurrent = healthCurrentRepository.findByMemberId(memberId).orElse(null);
                // 기록이 없는 경우
                if(healthCurrent == null) {
                    healthResponseDtoList.add(HealthResponseDto.builder()
                            .responseDate(tempDate)
                            .targetSteps(6000)
                            .nowSteps(0)
                            .build());
                }
                // 기록이 있는 경우
                else {
                    healthResponseDtoList.add(HealthResponseDto.builder()
                            .responseDate(tempDate)
                            .targetSteps(healthCurrent.getTargetSteps())
                            .nowSteps(healthCurrent.getNowSteps())
                            .build());
                }
            }
            // 오늘이 아닌 경우
            else {
                HealthHistory healthHistory = healthHistoryRepository.findByMemberIdAndRecordDate(memberId, tempDate).orElse(null);
                // 기록이 없는 경우
                if (healthHistory == null) {
                    healthResponseDtoList.add(HealthResponseDto.builder()
                            .responseDate(tempDate)
                            .targetSteps(6000)
                            .nowSteps(0)
                            .build());
                }
                // 기록이 있는 경우
                else {
                    healthResponseDtoList.add(HealthResponseDto.builder()
                            .responseDate(tempDate)
                            .targetSteps(healthHistory.getTargetSteps())
                            .nowSteps(healthHistory.getDaySteps())
                            .build());
                }
            }
        }
        return healthResponseDtoList;
    }

}
