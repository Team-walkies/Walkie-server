package site.walkies.walkie.global.probability;

import lombok.Getter;

@Getter
public enum CharacterProbability {
    // 확률의 경우 누적합으로 저장
    // ex) 해파리 획득 확률 0~20, 빨간 해파리 20~40, ...
    JELLYFISH("해파리", "무엇이든 시작에는 큰 용기가 필요해.\n" +
            "나와 함께 첫걸음을 내딛어보자.","JELLYFISH",0,0, 0, 10),
    RED_JELLYFISH("빨간 해파리", "부끄러우니까 보지 마." +
            "\n얼굴 빨개진단 말이야.","RED_JELLYFISH",0,0,1,20),
    GREEN_JELLYFISH("초록 해파리", "매생이를 너무 많이 먹었나 봐.\n" +
            "온몸이 초록빛이 됐어!", "GREEN_JELLYFISH",0,0,2,30),
    PURPLE_JELLYFISH("보라 해파리","나는 겁이 많은 성격이야…\n" +
            "그래서 얼굴이 늘 보랏빛으로 질려 있어…","PURPLE_JELLYFISH", 0,0,3,40),
    PINK_JELLYFISH("분홍 해파리", "몸을 분홍색으로 물들여 봤어.\n" +
            "어때? 나 예뻐?","PINK_JELLYFISH", 0,0,4,50),
    RABBIT_JELLYFISH("토끼 해파리","나는 달에서 내려왔어.\n" +
            "보름달이 뜨면 떡방아를 찧을 거야","RABBIT_JELLYFISH", 1,0,0,25),
    STARFISH_JELLYFISH("불가사리 해파리","불가사리가 붙은 지 어언 100년.\n" +
            "이것도 인연이니 같이 살지, 뭐.","STARFISH_JELLYFISH", 1,0,1,50),
    LIGHTNING_JELLYFISH("빠직 해파리","빠직! 빠직! \n" +
            "나와 함께라면 전기료 걱정은 없어.","LIGHTNING_JELLYFISH", 2,0,0,25),
    STRAWBERRY_JELLYFISH("딸기모찌 해파리","먹음직스러운 딸기 모찌…인 줄 알았지?\n" +
            "먹지 않게 조심해!","STRAWBERRY_JELLYFISH", 2,0,1,50),
    SPACE_JELLYFISH("우주 해파리","우주를 담은 광활한 몸, 무궁무진한 상상력.\n" +
            "그야말로 나는 절대적인 존재.","SPACE_JELLYFISH", 3,0,0,50),
    DINO("다이노", "나는 6천6백만 년을 기다려 너를 만났어.\n" +
            "우린 함께 걸을 운명인가 봐.","DINO", 0,1,0,60),
    RED_DINO("빨간 다이노", "화가 난다, 화가 나!\n" +
            "온몸이 울그락불그락 빨개졌어!","RED_DINO", 0,1,1,70),
    GREEN_DINO("민트 다이노","민트 좋아해?\n" +
            "내 옆에 있으면 상쾌해질 거야.","GREEN_DINO", 0,1,2,80),
    PURPLE_DINO("보라 다이노","추위를 많이 타서 온몸이 창백해졌어…\n" +
            "옷이라도 입혀줘.","PURPLE_DINO", 0,1,3,90),
    PINK_DINO("분홍 다이노","응? 자색 고구마라고?\n" +
            "나 고구마 아니고 공룡이야! ","PINK_DINO", 0,1,4,100),
    DEER_DINO("순록 다이노","얼른 크리스마스가 왔으면 좋겠다.\n" +
            "뭘 하는 지는 비밀이야.","DEER_DINO", 1,1,0,75),
    NESSIE_DINO("네시 다이노","난 수영이 특기야!\n" +
            "아... 여기선 걸어야 하나?","NESSIE_DINO", 1,1,1,100),
    PANCAKE_DINO("팬케이크 다이노","노릇노릇 맛있게 구워졌어.\n" +
            "나랑 걸으면 배고파질지도 몰라!","PANCAKE_DINO", 2,1,0,75),
    MELONSODA_DINO("메론소다 다이노", "걷다 보면 덥지 않아?\n" +
            "시원한 메론소다 마셔볼래?","MELONSODA_DINO", 2,1,1,100),
    DRAGON_DINO("드레곤 다이노", "나는 신비롭고 강력한 힘을 가진 드래곤.\n" +
            "어떤 것도 두렵지 않아.","DRAGON_DINO", 3,1,0,100);

    private final String name;
    private final String detail;
    private final String picUrl;

    // 일반 : 0, 희귀 : 1, 에픽 : 2, 전설 : 3
    private final int rank;
    // 해파리 : 0, 다이노 : 1
    private final int type;
    // 해파리 종류 구분
    private final int CharacterClass;
    private final double probability;

    CharacterProbability(final String name, final String detail,final String picUrl, final int rank, final int type, final int CharacterClass, final double probability) {
        this.name = name;
        this.detail = detail;
        this.picUrl = picUrl;
        this.rank = rank;
        this.type = type;
        this.CharacterClass = CharacterClass;
        this.probability = probability;
    }
}
