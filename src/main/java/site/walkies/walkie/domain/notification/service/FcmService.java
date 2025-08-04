package site.walkies.walkie.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.walkies.walkie.domain.notification.service.dto.request.FcmSendRequestDto;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService {

    private static final String TEST_TOKEN = "클라이언트 개발자에게 받은 디바이스 토큰을 여기에";

    @Transactional(readOnly = true)
    public void sendNotification(FcmSendRequestDto requestDto) {
        Message message = Message.builder()
                .setToken(requestDto.getToken())
                .setNotification(Notification.builder()
                        .setTitle(requestDto.getTitle())
                        .setBody(requestDto.getBody())
                        .build())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM 메시지 전송 성공: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 메시지 전송 실패", e);
            throw new CustomException(ErrorCode.FCM_SEND_FAILED);
        }
    }
}
