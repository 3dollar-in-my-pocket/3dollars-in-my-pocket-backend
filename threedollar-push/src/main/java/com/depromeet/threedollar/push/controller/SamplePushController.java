package com.depromeet.threedollar.push.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.threedollar.common.utils.JsonUtils;
import com.depromeet.threedollar.push.common.dto.response.ApiResponse;
import com.depromeet.threedollar.push.service.message.dto.request.SendFirebaseMessageBulkRequest;
import com.depromeet.threedollar.push.service.message.dto.request.SendFirebaseMessageRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SamplePushController {

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Value("${push.sqs.boss.single-push}")
    private String singlePushQueueUrl;

    @Value("${push.sqs.boss.bulk-push}")
    private String multiplePushQueueUrl;

    @PostMapping("/test/push/message")
    public ApiResponse<String> sendMessage(
        @Valid @RequestBody SendFirebaseMessageRequest request
    ) {
        Message<String> message = MessageBuilder
            .withPayload(JsonUtils.toJson(request))
            .build();
        queueMessagingTemplate.send(singlePushQueueUrl, message);
        return ApiResponse.OK;
    }

    @PostMapping("/test/push/message/bulk")
    public ApiResponse<String> sendMessage(
        @Valid @RequestBody SendFirebaseMessageBulkRequest request
    ) {
        Message<String> message = MessageBuilder
            .withPayload(JsonUtils.toJson(request))
            .build();
        queueMessagingTemplate.send(multiplePushQueueUrl, message);
        return ApiResponse.OK;
    }

}
