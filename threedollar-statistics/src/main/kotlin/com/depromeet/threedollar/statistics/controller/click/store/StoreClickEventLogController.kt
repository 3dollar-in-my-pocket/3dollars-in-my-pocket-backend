package com.depromeet.threedollar.statistics.controller.click.store

import com.depromeet.threedollar.statistics.controller.ApiResponse
import com.depromeet.threedollar.statistics.sevice.store.click.StoreClickEventLogService
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
class StoreClickEventLogController(
    private val storeClickEventLogService: StoreClickEventLogService
) {

    @GetMapping("/api/v1/click/store/{storeId}")
    fun retrieveStoreClicksCounts(
        @PathVariable storeId: Long,
        @RequestParam(required = false) after: LocalDateTime?
    ): ApiResponse<Long> {
        return ApiResponse.success(storeClickEventLogService.retrieveStoreClicksCounts(storeId, after))
    }

    @PostMapping("/api/v1/click/store/{storeId}")
    fun addStoreClickCount(
        @PathVariable storeId: Long,
        @RequestParam(required = false) userId: Long?,
        @RequestParam timestamp: Long
    ): ApiResponse<String> {
        // 일단 API로 만들어두고 차후 Consumer에서 받도록 변경
        storeClickEventLogService.addStoreClickEventLog(storeId, userId, timestamp)
        return ApiResponse.SUCCESS
    }

}
