package site.walkies.walkie.global.web.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 멤버 관련 예외
    // 아래는 예시입니다. 오류 상황에 맞는 이름과 메시지를 만들고, 사용할 HttpStatus를 선택하면 됩니다.
        USER_NOT_FOUND("존재하지 않는 유저입니다.", HttpStatus.UNAUTHORIZED),
    //    USER_VALIDATION_ERROR("유저 검증 오류가 발생했습니다.", HttpStatus.UNAUTHORIZED),
    //    INVALID_PASSWORD("올바르지 않은 비밀번호입니다.", HttpStatus.UNAUTHORIZED),
        INVALID_ACCESS_TOKEN("올바르지 않은 ACCESSTOKEN 입니다", HttpStatus.UNAUTHORIZED),
    //    JSON_PROCESSING_ERROR("JSON 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 알 관련 예외
    EGG_NOT_FOUND("해당 id를 가진 알이 없습니다.", HttpStatus.NOT_FOUND),

    // TMAP 관련 예외
    TMAP_SERVER_ERROR("TMAP API 조회에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 캐릭터 관련 예외
    CHARACTER_NOT_FOUND("해당 id를 가진 캐릭터가 없습니다.", HttpStatus.NOT_FOUND),
    // 공지 관련 예외

    // 리뷰 관련 예외
    FILE_SAVE_ERROR("파일 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_NOT_FOUND("파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REVIEW_NOT_FOUND("해당 id를 가진 리뷰가 없습니다.", HttpStatus.NOT_FOUND),

    // 스팟 관련 예외
    // 아래는 예시입니다. Enum이므로 마지막 예외에는 세미콜론 (;), 그 외에는 콤마(,)를 붙여줘야 합니다.
    SPOT_NOT_FOUND("해당 id를 가진 스팟이 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
