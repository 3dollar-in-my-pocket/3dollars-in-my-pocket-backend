package com.depromeet.threedollar.api.bossservice.controller.push

import javax.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.infrastructure.sqs.common.type.TopicType
import com.depromeet.threedollar.infrastructure.sqs.dto.payload.SendFirebaseMessageBulkPayload
import com.depromeet.threedollar.infrastructure.sqs.dto.payload.SendFirebaseMessagePayload
import com.depromeet.threedollar.infrastructure.sqs.provider.SqsSender

@Profile("local", "local-docker", "dev")
@RestController
class PushTestController(
    private val sqsSender: SqsSender,
) {

    @PostMapping("/test/push/message")
    fun sendMessage(
        @Valid @RequestBody payload: SendFirebaseMessagePayload,
    ): ApiResponse<String> {
        sqsSender.sendToTopic(TopicType.SINGLE_APP_PUSH, payload)
        return ApiResponse.OK
    }

    @PostMapping("/test/push/message/bulk")
    fun sendMessageBulk(
        @Valid @RequestBody payload: SendFirebaseMessageBulkPayload,
    ): ApiResponse<String> {
        sqsSender.sendToTopic(TopicType.BULK_APP_PUSH, payload)
        return ApiResponse.OK
    }

}
