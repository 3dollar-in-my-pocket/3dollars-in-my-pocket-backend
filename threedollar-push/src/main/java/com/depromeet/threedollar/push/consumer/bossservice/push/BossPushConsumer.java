package com.depromeet.threedollar.push.consumer.bossservice.push;

import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.common.utils.JsonUtils;
import com.depromeet.threedollar.infrastructure.sqs.provider.dto.request.SendBulkPushRequest;
import com.depromeet.threedollar.infrastructure.sqs.provider.dto.request.SendSinglePushRequest;
import com.depromeet.threedollar.push.provider.push.PushProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class BossPushConsumer {

    private final PushProvider pushProvider;

    @SqsListener(value = "${push.sqs.boss.single-push}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    public void consumeBossServiceSinglePush(@Payload String payload, @Headers Map<String, String> headers) {
        SendSinglePushRequest request = JsonUtils.toObject(payload, SendSinglePushRequest.class);
        if (log.isDebugEnabled()) {
            log.debug("단건 푸시를 발송합니다 request: {} headers: {}", request, headers);
        }
        pushProvider.sendMessageAsync(ApplicationType.BOSS_API, request);
    }

    @SqsListener(value = "${push.sqs.boss.bulk-push}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    public void consumeBossServiceBulkPush(@Payload String payload, @Headers Map<String, String> headers) {
        SendBulkPushRequest request = JsonUtils.toObject(payload, SendBulkPushRequest.class);
        if (log.isDebugEnabled()) {
            log.debug("벌크 푸시를 발송합니다 request: {} headers: {}", request, headers);
        }
        pushProvider.sendMessageBulkAsync(ApplicationType.BOSS_API, request);
    }

}
