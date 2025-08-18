package site.walkies.walkie.domain.health.enums;

import lombok.Getter;

@Getter
public enum Calorie {
    AIR(0,"공기", "들숨 날숨~ 숨쉬기 운동 중!", "AIR"),
    CANDY(200, "사탕 1개", "이제 몸이 풀렸어요!", "CANDY"),
    BANANA(2000,"바나나 1개","슬슬 운동한 느낌 나죠?", "BANANA"),
    GIMBAP(4000,"삼각김밥 1개", "편의점 인기템 클리어!", "GIMBAP"),
    CHICKEN(6000, "닭다리 1개", "와, 간식 하나정도는 먹어도 되겠어요", "CHICKEN"),
    PASTA(10000, "파스타 1접시", "대단해요! 당신의 다리에게 박수를!", "PASTA");

    private final double calories;
    private final String foodName;
    private final String foodDescription;
    private final String imageUrl;

    Calorie(double calories,String foodName, String foodDescription, String imageUrl) {
        this.calories = calories;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.imageUrl = imageUrl;
    }
}
