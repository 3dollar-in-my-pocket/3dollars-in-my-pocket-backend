package com.depromeet.threedollar.api.bossservice.controller.push

import javax.validation.Valid
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.context.annotation.Profile
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.common.utils.JsonUtils
import com.depromeet.threedollar.infrastructure.sqs.property.BossSqsQueueProperty
import com.depromeet.threedollar.infrastructure.sqs.push.dto.payload.SendFirebaseMessageBulkPayload
import com.depromeet.threedollar.infrastructure.sqs.push.dto.payload.SendFirebaseMessagePayload

@Profile("local", "local-docker", "dev")
@RestController
class PushTestController(
    private val queueMessagingTemplate: QueueMessagingTemplate,
    private val bossSqsQueueProperty: BossSqsQueueProperty,
) {

    @PostMapping("/test/push/message")
    fun sendMessage(
        @Valid @RequestBody request: SendFirebaseMessagePayload,
    ): ApiResponse<String> {
        val message = MessageBuilder
            .withPayload(JsonUtils.toJson(request))
            .build()
        queueMessagingTemplate.send(bossSqsQueueProperty.singlePush, message)
        return ApiResponse.OK
    }

    @PostMapping("/test/push/message/bulk")
    fun sendMessageBulk(
        @Valid @RequestBody request: SendFirebaseMessageBulkPayload,
    ): ApiResponse<String> {
        val message = MessageBuilder
            .withPayload(JsonUtils.toJson(request))
            .build()
        queueMessagingTemplate.send(bossSqsQueueProperty.bulkPush, message)
        return ApiResponse.OK
    }

}
