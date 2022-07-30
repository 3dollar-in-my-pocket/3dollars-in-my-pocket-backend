package com.depromeet.threedollar.api.bossservice.controller.push

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.infrastructure.sqs.common.type.TopicType
import com.depromeet.threedollar.infrastructure.sqs.provider.MessageSendProvider
import com.depromeet.threedollar.infrastructure.sqs.provider.dto.request.SendBulkPushRequest
import com.depromeet.threedollar.infrastructure.sqs.provider.dto.request.SendSinglePushRequest
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Profile("local", "local-docker", "dev")
@RestController
class PushTestController(
    private val messageSendProvider: MessageSendProvider,
) {

    @PostMapping("/test/push/message")
    fun sendMessage(
        @Valid @RequestBody payload: SendSinglePushRequest,
    ): ApiResponse<String> {
        messageSendProvider.sendToTopic(TopicType.BOSS_SINGLE_APP_PUSH, payload)
        return ApiResponse.OK
    }

    @PostMapping("/test/push/message/bulk")
    fun sendMessageBulk(
        @Valid @RequestBody payload: SendBulkPushRequest,
    ): ApiResponse<String> {
        messageSendProvider.sendToTopic(TopicType.BOSS_BULK_APP_PUSH, payload)
        return ApiResponse.OK
    }

}
