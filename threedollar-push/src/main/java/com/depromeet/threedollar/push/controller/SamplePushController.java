package com.depromeet.threedollar.push.controller;

import com.depromeet.threedollar.infrastructure.sqs.common.type.TopicType;
import com.depromeet.threedollar.infrastructure.sqs.provider.MessageSendProvider;
import com.depromeet.threedollar.infrastructure.sqs.provider.dto.request.SendBulkPushRequest;
import com.depromeet.threedollar.infrastructure.sqs.provider.dto.request.SendSinglePushRequest;
import com.depromeet.threedollar.push.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Profile({"local", "local-docker", "dev"})
@RequiredArgsConstructor
@RestController
public class SamplePushController {

    private final MessageSendProvider messageSendProvider;

    @PostMapping("/test/push/message")
    public ApiResponse<String> sendMessage(
        @Valid @RequestBody SendSinglePushRequest payload
    ) {
        messageSendProvider.sendToTopic(TopicType.BOSS_SINGLE_APP_PUSH, payload);
        return ApiResponse.OK;
    }

    @PostMapping("/test/push/message/bulk")
    public ApiResponse<String> sendMessage(
        @Valid @RequestBody SendBulkPushRequest payload
    ) {
        messageSendProvider.sendToTopic(TopicType.BOSS_BULK_APP_PUSH, payload);
        return ApiResponse.OK;
    }

}
