package com.depromeet.threedollar.boss.api.controller.dto.response

import com.depromeet.threedollar.document.common.document.BaseDocument
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
