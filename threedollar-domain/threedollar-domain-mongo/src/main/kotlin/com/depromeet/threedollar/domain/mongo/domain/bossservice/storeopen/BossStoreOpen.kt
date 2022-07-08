package com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen

import java.time.LocalDateTime
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import com.depromeet.threedollar.domain.mongo.core.model.BaseDocument

@Document("boss_store_open_v1")
class BossStoreOpen(
    val bossStoreId: String,

    val openStartDateTime: LocalDateTime,

    @Indexed(expireAfterSeconds = 60 * 30) // 30분 만료
    var expiredAt: LocalDateTime,
) : BaseDocument() {

    fun updateExpiredAt(expiredAt: LocalDateTime) {
        this.expiredAt = expiredAt
    }

    companion object {
        fun of(
            bossStoreId: String,
            dateTime: LocalDateTime,
        ): BossStoreOpen {
            return BossStoreOpen(
                bossStoreId = bossStoreId,
                openStartDateTime = dateTime,
                expiredAt = dateTime
            )
        }
    }

}


