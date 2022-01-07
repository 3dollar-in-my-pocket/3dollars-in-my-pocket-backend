package com.depromeet.threedollar.consumer.sevice.store.click

import com.depromeet.threedollar.common.utils.LocalDateTimeUtils
import com.depromeet.threedollar.consumer.document.click.store.StoreClickEventLog
import com.depromeet.threedollar.consumer.document.click.store.StoreClickEventLogRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class StoreClickEventLogService(
    private val storeClickEventLogRepository: StoreClickEventLogRepository
) {

    fun addStoreClickEventLog(
        storeId: Long,
        userId: Long?,
        timestamp: Long
    ) {
        storeClickEventLogRepository.save(StoreClickEventLog.newInstance(storeId, userId, timestamp))
    }

    fun retrieveStoreClicksCounts(
        storeId: Long,
        afterDateTime: LocalDateTime?
    ): Long {
        return afterDateTime?.let {
            storeClickEventLogRepository.countByStoreIdAndTimestampGreaterThanEqual(storeId, LocalDateTimeUtils.convertToTimestamp(afterDateTime))
        } ?: storeClickEventLogRepository.countByStoreId(storeId)
    }

}
