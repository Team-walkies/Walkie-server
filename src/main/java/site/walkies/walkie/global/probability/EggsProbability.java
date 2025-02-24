package site.walkies.walkie.global.probability;
import lombok.Getter;

@Getter
public enum EggsProbability {
    // 확률의 경우 누적합으로 저장
    // ex) 알 획득 확률 normal 0~65, rare 65~85, ...
    NORMAL_EGG(0,65.0,80.0, 95.0, 99.0,100.0),
    RARE_EGG(1,85.0,50.0, 85.0, 90.0,100.0),
    EPIC_EGG(2,97.0,20.0, 60.0, 90.0,100.0),
    LEGENDARY_EGG(3,100.0,5.0, 30.0, 70.0,100.0)
    ;

    private final int rank;
    private final double probability;
    private final double normalProbability;
    private final double rareProbability;
    private final double epicProbability;
    private final double legendaryProbability;

    EggsProbability(int rank, double probability, double normalProbability, double rareProbability, double epicProbability, double legendaryProbability) {
        this.rank = rank;
        this.probability = probability;
        this.normalProbability = normalProbability;
        this.rareProbability = rareProbability;
        this.epicProbability = epicProbability;
        this.legendaryProbability = legendaryProbability;
    }
}