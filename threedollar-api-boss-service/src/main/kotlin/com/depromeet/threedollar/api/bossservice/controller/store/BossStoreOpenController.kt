package com.depromeet.threedollar.api.bossservice.controller.store

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.bossservice.config.interceptor.Auth
import com.depromeet.threedollar.api.bossservice.config.resolver.BossId
import com.depromeet.threedollar.api.bossservice.service.store.BossStoreOpenService
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.config.resolver.MapLocation
import com.depromeet.threedollar.common.model.LocationValue
import io.swagger.annotations.ApiOperation

@RestController
class BossStoreOpenController(
    private val bossStoreOpenService: BossStoreOpenService,
) {

    @ApiOperation("[인증] 가게를 영업 정보를 시작/재시작 합니다", notes = "30분간 갱신하지 않으면 자동 영업 종료 처리되니 주기적으로 갱신해야 합니다.")
    @Auth
    @PostMapping("/v1/boss/store/{bossStoreId}/open")
    fun openBossStore(
        @PathVariable bossStoreId: String,
        @BossId bossId: String,
        @MapLocation mapLocation: LocationValue,
    ): ApiResponse<String> {
        bossStoreOpenService.openBossStore(bossStoreId, bossId, mapLocation)
        return ApiResponse.OK
    }

    @ApiOperation("[인증] 가게를 영업 정보를 갱신합니다.", notes = "영업 중이지 않은 경우 403 에러 발생합니다 + 30분간 갱신하지 않으면 자동 영업 종료 처리되니 주기적으로 갱신해야 합니다.")
    @Auth
    @PutMapping("/v1/boss/store/{bossStoreId}/renew")
    fun patchBossStore(
        @PathVariable bossStoreId: String,
        @BossId bossId: String,
        @MapLocation mapLocation: LocationValue,
    ): ApiResponse<String> {
        bossStoreOpenService.renewBossStoreOpenInfo(bossStoreId, bossId, mapLocation)
        return ApiResponse.OK
    }

    @ApiOperation("[인증] 가게를 강제로 영업 종료합니다")
    @Auth
    @DeleteMapping("/v1/boss/store/{bossStoreId}/close")
    fun closeBossStore(
        @PathVariable bossStoreId: String,
        @BossId bossId: String,
    ): ApiResponse<String> {
        bossStoreOpenService.closeBossStore(bossStoreId, bossId)
        return ApiResponse.OK
    }

}
