package site.walkies.walkie.global.web.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.walkies.walkie.global.web.dto.response.ExceptionResponse;
import site.walkies.walkie.global.webhook.DiscordNotifier;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final DiscordNotifier discordNotifier;

    // @RestControllerAdvice와 @ExceptionHandler를 활용하여 컨트롤러 단에서 발생하는 예외를 중앙에서 처리

    // CustomException에 대한 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(CustomException ex){
        log.error("CustomException 발생: ", ex);
        ExceptionResponse response = ExceptionResponse.builder()
                .status(ex.getErrorCode().getHttpStatus().value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, ex.getErrorCode().getHttpStatus());
    }

    // 그 외의 모든 Exception에 대한 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralException(Exception ex){
        log.error("Exception 발생: ", ex);

        // 디스코드 전송
        String errorLog = "**서버 오류 발생!**\n"
                + "- 메시지: " + ex.getMessage() + "\n"
                + "- 클래스: " + ex.getClass().getName();

        discordNotifier.sendMessage(errorLog);

        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("서버 내부 오류가 발생했습니다.")
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
