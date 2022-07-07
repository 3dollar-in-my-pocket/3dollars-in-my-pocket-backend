package com.depromeet.threedollar.push.controller;

import javax.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.threedollar.infrastructure.sqs.common.type.TopicType;
import com.depromeet.threedollar.infrastructure.sqs.dto.payload.SendFirebaseMessageBulkPayload;
import com.depromeet.threedollar.infrastructure.sqs.dto.payload.SendFirebaseMessagePayload;
import com.depromeet.threedollar.infrastructure.sqs.provider.SqsSender;
import com.depromeet.threedollar.push.common.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@Profile({"local", "local-docker", "dev"})
@RequiredArgsConstructor
@RestController
public class SamplePushController {

    private final SqsSender sqsSender;

    @PostMapping("/test/push/message")
    public ApiResponse<String> sendMessage(
        @Valid @RequestBody SendFirebaseMessagePayload payload
    ) {
        sqsSender.sendToTopic(TopicType.SINGLE_APP_PUSH, payload);
        return ApiResponse.OK;
    }

    @PostMapping("/test/push/message/bulk")
    public ApiResponse<String> sendMessage(
        @Valid @RequestBody SendFirebaseMessageBulkPayload payload
    ) {
        sqsSender.sendToTopic(TopicType.BULK_APP_PUSH, payload);
        return ApiResponse.OK;
    }

}
