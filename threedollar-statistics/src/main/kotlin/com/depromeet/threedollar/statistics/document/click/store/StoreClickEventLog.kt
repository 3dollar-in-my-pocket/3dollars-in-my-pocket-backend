package com.depromeet.threedollar.statistics.document.click.store

import com.depromeet.threedollar.common.utils.UuidUtils
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "store_click_event_log_v1")
class StoreClickEventLog(
    @Id
    val id: String,

    val storeId: Long,
    val userId: Long?,
    val timestamp: Long
) {

    companion object {
        fun newInstance(storeId: Long, userId: Long?, timestamp: Long): StoreClickEventLog {
            return StoreClickEventLog(
                id = UuidUtils.generate(),
                storeId = storeId,
                userId = userId,
                timestamp = timestamp
            )
        }
    }

}
