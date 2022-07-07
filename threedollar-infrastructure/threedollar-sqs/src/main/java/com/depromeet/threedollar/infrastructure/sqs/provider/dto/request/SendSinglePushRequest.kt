package com.depromeet.threedollar.infrastructure.sqs.provider.dto.request

import com.depromeet.threedollar.common.type.PushOptions
import com.depromeet.threedollar.common.type.template.PushMessageTemplateType

data class SendSinglePushRequest(
    val token: String,
    val title: String,
    val body: String,
    val pushOptions: PushOptions,
) {

    companion object {
        fun of(token: String, messageTemplateType: PushMessageTemplateType): SendSinglePushRequest {
            return SendSinglePushRequest(
                token = token,
                title = messageTemplateType.title,
                body = messageTemplateType.body,
                pushOptions = messageTemplateType.pushOptions,
            )
        }
    }

}
