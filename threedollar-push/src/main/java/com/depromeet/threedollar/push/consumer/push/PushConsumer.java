package com.depromeet.threedollar.push.consumer.push;

import java.util.Map;

import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.common.utils.JsonUtils;
import com.depromeet.threedollar.infrastructure.sqs.dto.payload.SendFirebaseMessageBulkPayload;
import com.depromeet.threedollar.infrastructure.sqs.dto.payload.SendFirebaseMessagePayload;
import com.depromeet.threedollar.push.provider.push.PushProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class PushConsumer {

    private final PushProvider pushProvider;

    @SqsListener(value = "${push.sqs.boss.single-push}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    public void consumeBossPushMessage(@Payload String payload, @Headers Map<String, String> headers) {
        SendFirebaseMessagePayload request = JsonUtils.toObject(payload, SendFirebaseMessagePayload.class);
        if (log.isDebugEnabled()) {
            log.debug("단건 푸시를 발송합니다 request: {} headers: {}", request, headers);
        }
        pushProvider.sendMessageAsync(ApplicationType.BOSS_API, request);
    }

    @SqsListener(value = "${push.sqs.boss.bulk-push}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    public void consumeBossPushBulkMessage(@Payload String payload, @Headers Map<String, String> headers) {
        SendFirebaseMessageBulkPayload request = JsonUtils.toObject(payload, SendFirebaseMessageBulkPayload.class);
        if (log.isDebugEnabled()) {
            log.debug("벌크 푸시를 발송합니다 request: {} headers: {}", request, headers);
        }
        pushProvider.sendMessageBulkAsync(ApplicationType.BOSS_API, request);
    }

}
