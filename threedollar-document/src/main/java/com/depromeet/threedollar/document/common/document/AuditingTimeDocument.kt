package com.depromeet.threedollar.document.common.document

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

abstract class AuditingTimeDocument {

    @CreatedDate
    var createdDateTime: LocalDateTime? = null

    @LastModifiedDate
    var updatedDateTime: LocalDateTime? = null

}
