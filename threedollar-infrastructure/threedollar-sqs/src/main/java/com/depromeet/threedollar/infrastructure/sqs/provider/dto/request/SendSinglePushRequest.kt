package com.depromeet.threedollar.infrastructure.sqs.provider.dto.request

import com.depromeet.threedollar.common.type.PushOptionsType
import com.depromeet.threedollar.common.type.template.PushMessageTemplateType

data class SendSinglePushRequest(
    val token: String,
    val title: String,
    val body: String,
    val pushOptions: PushOptionsType,
) {

    companion object {
        fun of(
            token: String,
            messageTemplateType: PushMessageTemplateType,
            customBodyMessage: String? = null,
        ): SendSinglePushRequest {
            return SendSinglePushRequest(
                token = token,
                title = messageTemplateType.title,
                body = customBodyMessage ?: messageTemplateType.body,
                pushOptions = messageTemplateType.pushOptions,
            )
        }
    }

}
