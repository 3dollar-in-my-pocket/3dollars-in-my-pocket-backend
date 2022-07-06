package com.depromeet.threedollar.push.consumer.push;

import java.util.Map;

import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.depromeet.threedollar.common.utils.JsonUtils;
import com.depromeet.threedollar.push.infra.firebase.FirebaseAppType;
import com.depromeet.threedollar.push.service.message.FirebaseMessageService;
import com.depromeet.threedollar.push.service.message.dto.request.SendFirebaseMessageBulkRequest;
import com.depromeet.threedollar.push.service.message.dto.request.SendFirebaseMessageRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class PushConsumer {

    private final FirebaseMessageService firebaseMessageService;

    @SqsListener(value = "${push.sqs.boss.single-push}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    public void consumeBossPushMessage(@Payload String payload, @Headers Map<String, String> headers) {
        SendFirebaseMessageRequest request = JsonUtils.toObject(payload, SendFirebaseMessageRequest.class);
        if (log.isDebugEnabled()) {
            log.debug("단건 푸시를 발송합니다 request: {} headers: {}", request, headers);
        }
        firebaseMessageService.sendMessageAsync(FirebaseAppType.BOSS, request);
    }

    @SqsListener(value = "${push.sqs.boss.bulk-push}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    public void consumeBossPushBulkMessage(@Payload String payload, @Headers Map<String, String> headers) {
        SendFirebaseMessageBulkRequest request = JsonUtils.toObject(payload, SendFirebaseMessageBulkRequest.class);
        if (log.isDebugEnabled()) {
            log.debug("벌크 푸시를 발송합니다 request: {} headers: {}", request, headers);
        }
        firebaseMessageService.sendMessageBulkAsync(FirebaseAppType.BOSS, request);
    }

}
