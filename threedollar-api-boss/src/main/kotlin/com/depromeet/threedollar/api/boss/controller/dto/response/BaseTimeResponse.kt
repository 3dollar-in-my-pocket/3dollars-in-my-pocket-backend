package com.depromeet.threedollar.api.boss.controller.dto.response

import com.depromeet.threedollar.domain.mongo.common.domain.BaseDocument
import java.time.LocalDateTime

open class BaseTimeResponse(
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null
) {

    fun setBaseTime(baseDocument: BaseDocument) {
        this.createdAt = baseDocument.createdAt
        this.updatedAt = baseDocument.updatedAt
    }

}
