package com.depromeet.threedollar.api.bossservice.controller.push

import javax.validation.Valid
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.common.utils.JsonUtils
import com.depromeet.threedollar.infrastructure.sqs.property.BossSqsQueueProperty
import com.depromeet.threedollar.infrastructure.sqs.push.dto.payload.SendFirebaseMessagePayload

@RestController
class PushTestController(
    private val queueMessagingTemplate: QueueMessagingTemplate,
    private val bossSqsQueueProperty: BossSqsQueueProperty,
) {

    @PostMapping("/test/push/message")
    fun sendMessage(
        @Valid @RequestBody request: SendFirebaseMessagePayload,
    ): ApiResponse<String?>? {
        val message = MessageBuilder
            .withPayload(JsonUtils.toJson(request))
            .build()
        queueMessagingTemplate.send(bossSqsQueueProperty.singlePush, message)
        return ApiResponse.OK
    }

}
