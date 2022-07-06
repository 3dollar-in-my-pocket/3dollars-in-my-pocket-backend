package com.depromeet.threedollar.push.controller;

import javax.validation.Valid;

import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.threedollar.common.utils.JsonUtils;
import com.depromeet.threedollar.infrastructure.sqs.property.BossSqsQueueProperty;
import com.depromeet.threedollar.infrastructure.sqs.push.dto.payload.SendFirebaseMessageBulkPayload;
import com.depromeet.threedollar.infrastructure.sqs.push.dto.payload.SendFirebaseMessagePayload;
import com.depromeet.threedollar.push.common.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SamplePushController {

    private final QueueMessagingTemplate queueMessagingTemplate;
    private final BossSqsQueueProperty bossSqsQueueProperty;

    @PostMapping("/test/push/message")
    public ApiResponse<String> sendMessage(
        @Valid @RequestBody SendFirebaseMessagePayload request
    ) {
        Message<String> message = MessageBuilder
            .withPayload(JsonUtils.toJson(request))
            .build();
        queueMessagingTemplate.send(bossSqsQueueProperty.getSinglePush(), message);
        return ApiResponse.OK;
    }

    @PostMapping("/test/push/message/bulk")
    public ApiResponse<String> sendMessage(
        @Valid @RequestBody SendFirebaseMessageBulkPayload request
    ) {
        Message<String> message = MessageBuilder
            .withPayload(JsonUtils.toJson(request))
            .build();
        queueMessagingTemplate.send(bossSqsQueueProperty.getBulkPush(), message);
        return ApiResponse.OK;
    }

}
