package site.walkies.walkie.global.probability;

import lombok.Getter;

@Getter
public enum CharacterProbability {
    // 확률의 경우 누적합으로 저장
    // ex) 해파리 획득 확률 0~20, 빨간 해파리 20~40, ...
    JELLYFISH("해파리", 0,0, 0, 10),
    RED_JELLYFISH("빨간 해파리",0,0,1,20),
    GREEN_JELLYFISH("초록 해파리",0,0,2,30),
    PURPLE_JELLYFISH("보라 해파리",0,0,3,40),
    PINK_JELLYFISH("분홍 해파리",0,0,4,50),
    RABBIT_JELLYFISH("토끼 해파리",1,0,0,25),
    STARFISH_JELLYFISH("불가사리 해파리",1,0,1,50),
    LIGHTNING_JELLYFISH("빠직 해파리",2,0,0,25),
    STRAWBERRY_JELLYFISH("딸기모찌 해파리",2,0,1,50),
    SPACE_JELLYFISH("우주 해파리",3,0,0,50),
    DINO("다이노",0,1,0,60),
    RED_DINO("빨간 다이노",0,1,1,70),
    GREEN_DINO("초록 다이노",0,1,2,80),
    PURPLE_DINO("보라 다이노",0,1,3,90),
    PINK_DINO("분홍 다이노",0,1,4,100),
    DEER_DINO("순록 다이노",1,1,0,75),
    NESSIE_DINO("네시 다이노",1,1,1,100),
    PANCAKE_DINO("팬케이크 다이노",2,1,0,75),
    MELONSODA_DINO("메론소다 다이노",2,1,1,100),
    DRAGON_DINO("드레곤 다이노",3,1,0,100);

    private final String name;
    // 일반 : 0, 희귀 : 1, 에픽 : 2, 전설 : 3
    private final int rank;
    // 해파리 : 0, 다이노 : 1
    private final int type;
    // 해파리 종류 구분
    private final int CharacterClass;
    private final double probability;

    CharacterProbability(final String name, final int rank, final int type, final int CharacterClass, final double probability) {
        this.name = name;
        this.rank = rank;
        this.type = type;
        this.CharacterClass = CharacterClass;
        this.probability = probability;
    }
}
