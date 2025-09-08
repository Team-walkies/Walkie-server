package site.walkies.walkie.global.web.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 멤버 관련 예외
    // 아래는 예시입니다. 오류 상황에 맞는 이름과 메시지를 만들고, 사용할 HttpStatus를 선택하면 됩니다.
    USER_NOT_FOUND("존재하지 않는 유저입니다.", HttpStatus.UNAUTHORIZED),
    ALREADY_REGISTERED_USER("이미 계정이 등록된 유저입니다.", HttpStatus.CONFLICT),
    INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
    //    USER_VALIDATION_ERROR("유저 검증 오류가 발생했습니다.", HttpStatus.UNAUTHORIZED),
    //    INVALID_PASSWORD("올바르지 않은 비밀번호입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("올바르지 않은 ACCESSTOKEN 입니다", HttpStatus.UNAUTHORIZED),
    //    JSON_PROCESSING_ERROR("JSON 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DELETED_USER_CANNOT_LOGIN("탈퇴 유예 중인 유저는 로그인 할 수 없습니다. ", HttpStatus.UNAUTHORIZED),
    DELETED_USER_CANNOT_ACCESS("탈퇴한 유저는 접근할 수 없습니다.", HttpStatus.UNAUTHORIZED),

    // 알 관련 예외
    EGG_NOT_FOUND("해당 id를 가진 알이 없습니다.", HttpStatus.NOT_FOUND),
    EGG_ALREADY_GET("오늘은 이미 알을 지급 받았습니다.", HttpStatus.TOO_MANY_REQUESTS),

    // TMAP 관련 예외
    TMAP_SERVER_ERROR("TMAP API 조회에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 캐릭터 관련 예외
    CHARACTER_NOT_FOUND("해당 id를 가진 캐릭터가 없습니다.", HttpStatus.NOT_FOUND),
    CHARACTER_TYPE_NOT_FOUND("해당 종류의 캐릭터가 없습니다.", HttpStatus.NOT_FOUND),
    // 공지 관련 예외

    // 리뷰 관련 예외
    FILE_SAVE_ERROR("파일 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_NOT_FOUND("파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REVIEW_NOT_FOUND("해당 id를 가진 리뷰가 없습니다.", HttpStatus.NOT_FOUND),
    PARSING_ERROR("json 형식을 확인해주세요. 파싱에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    REVIEW_NOT_USER("해당 리뷰의 작성자가 아니므로, 변경 불가능합니다.", HttpStatus.NOT_ACCEPTABLE),

    // FCM 관련 예외
    FCM_TOKEN_VALUE_REQUIRED("FCM 토큰을 필수로 넣어야 합니다.", HttpStatus.UNAUTHORIZED),
    FCM_SEND_FAILED("FCM 메시지 전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 스팟 관련 예외
    // 아래는 예시입니다. Enum이므로 마지막 예외에는 세미콜론 (;), 그 외에는 콤마(,)를 붙여줘야 합니다.
    SPOT_NOT_FOUND("해당 id를 가진 스팟이 없습니다.", HttpStatus.NOT_FOUND),

    // 헬스케어 관련 예외
    DATE_OVER_ERROR("오늘 이후의 날짜는 검색이 불가능 합니다.", HttpStatus.INTERNAL_SERVER_ERROR),;

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
