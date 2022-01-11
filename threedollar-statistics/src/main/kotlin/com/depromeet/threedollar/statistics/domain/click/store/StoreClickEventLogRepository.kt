package com.depromeet.threedollar.statistics.domain.click.store

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface StoreClickEventLogRepository : ElasticsearchRepository<StoreClickEventLog, String> {

    fun countByStoreId(storeId: Long): Long

    fun countByStoreIdAndTimestampGreaterThanEqual(storeId: Long, timestamp: Long): Long

}