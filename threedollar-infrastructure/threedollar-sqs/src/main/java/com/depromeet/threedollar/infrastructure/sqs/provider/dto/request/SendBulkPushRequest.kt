package com.depromeet.threedollar.infrastructure.sqs.provider.dto.request

import com.depromeet.threedollar.common.type.PushOptionsType
import javax.validation.Valid
import javax.validation.constraints.Size

data class SendBulkPushRequest(
    @Valid @Size(min = 1, max = 500)
    val tokens: Set<String>,
    val title: String,
    val body: String,
    val pushOptions: PushOptionsType,
) {

    companion object {
        fun backgroundPush(tokens: Set<String>): SendBulkPushRequest {
            return SendBulkPushRequest(
                tokens = tokens,
                title = "",
                body = "",
                pushOptions = PushOptionsType.BACKGROUND,
            )
        }
    }

}
