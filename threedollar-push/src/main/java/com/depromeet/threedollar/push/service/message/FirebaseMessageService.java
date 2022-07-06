package com.depromeet.threedollar.push.service.message;

import org.springframework.stereotype.Service;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.push.infra.firebase.FirebaseAppType;
import com.depromeet.threedollar.push.infra.firebase.FirebaseMessagingFinder;
import com.depromeet.threedollar.push.service.message.dto.request.SendFirebaseMessageBulkRequest;
import com.depromeet.threedollar.push.service.message.dto.request.SendFirebaseMessageRequest;
import com.google.firebase.messaging.FirebaseMessaging;

@Service
public class FirebaseMessageService {

    public void sendMessageAsync(FirebaseAppType firebaseAppType, SendFirebaseMessageRequest request) {
        try {
            FirebaseMessaging messaging = FirebaseMessagingFinder.find(firebaseAppType);
            messaging.sendAsync(request.toMessage());
        } catch (Exception e) {
            throw new InternalServerException(String.format("Firebase 단건 메시지를 전송하는 중 에러가 발생하였습니다. message: (%s)", e.getMessage()));
        }
    }

    public void sendMessageBulkAsync(FirebaseAppType firebaseAppType, SendFirebaseMessageBulkRequest request) {
        try {
            FirebaseMessaging messaging = FirebaseMessagingFinder.find(firebaseAppType);
            messaging.sendAllAsync(request.toMessages());
        } catch (Exception e) {
            throw new InternalServerException(String.format("Firebase 다건 메시지를 전송하는 중 에러가 발생하였습니다. message: (%s)", e.getMessage()));
        }
    }

}
