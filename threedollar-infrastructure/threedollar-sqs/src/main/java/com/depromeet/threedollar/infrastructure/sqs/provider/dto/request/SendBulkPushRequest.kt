package com.depromeet.threedollar.infrastructure.sqs.provider.dto.request

import com.depromeet.threedollar.common.type.PushOptions
import javax.validation.Valid
import javax.validation.constraints.Size

data class SendBulkPushRequest(
    @Valid @Size(min = 1, max = 500)
    val tokens: Set<String>,
    val title: String,
    val body: String,
    val pushOptions: PushOptions,
)
