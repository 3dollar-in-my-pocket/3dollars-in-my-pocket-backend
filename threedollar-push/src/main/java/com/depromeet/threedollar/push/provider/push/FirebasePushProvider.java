package com.depromeet.threedollar.push.provider.push;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.infrastructure.firebase.FirebaseMessagingFinder;
import com.depromeet.threedollar.infrastructure.sqs.dto.payload.SendFirebaseMessageBulkPayload;
import com.depromeet.threedollar.infrastructure.sqs.dto.payload.SendFirebaseMessagePayload;
import com.depromeet.threedollar.push.common.constants.PushExtraFieldConstants;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class FirebasePushProvider implements PushProvider {

    @Override
    public void sendMessageAsync(
        @NotNull ApplicationType applicationType,
        @NotNull SendFirebaseMessagePayload request
    ) {
        try {
            FirebaseMessaging messaging = FirebaseMessagingFinder.find(applicationType);
            messaging.sendAsync(toMessage(request));
        } catch (Exception e) {
            throw new InternalServerException(String.format("Firebase 단건 메시지를 전송하는 중 에러가 발생하였습니다. message: (%s)", e.getMessage()));
        }
    }

    private Message toMessage(SendFirebaseMessagePayload request) {
        return Message.builder()
            .setNotification(Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build())
            .setToken(request.getToken())
            .putData(PushExtraFieldConstants.PUSH_OPTIONS, request.getPushOptions().name())
            .build();
    }

    @Override
    public void sendMessageBulkAsync(
        @NotNull ApplicationType applicationType,
        @NotNull SendFirebaseMessageBulkPayload request
    ) {
        try {
            FirebaseMessaging messaging = FirebaseMessagingFinder.find(applicationType);
            messaging.sendAllAsync(toMessages(request));
        } catch (Exception e) {
            throw new InternalServerException(String.format("Firebase 다건 메시지를 전송하는 중 에러가 발생하였습니다. message: (%s)", e.getMessage()));
        }
    }

    private List<Message> toMessages(SendFirebaseMessageBulkPayload request) {
        return request.getTokens().stream()
            .map(token -> toMessage(token, request))
            .collect(Collectors.toList());
    }

    private Message toMessage(String token, SendFirebaseMessageBulkPayload request) {
        return Message.builder()
            .setNotification(Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build())
            .setToken(token)
            .putData(PushExtraFieldConstants.PUSH_OPTIONS, request.getPushOptions().name())
            .build();
    }

}
