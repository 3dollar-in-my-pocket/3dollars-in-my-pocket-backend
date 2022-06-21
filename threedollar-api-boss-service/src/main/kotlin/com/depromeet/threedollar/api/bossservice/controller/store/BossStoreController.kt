package com.depromeet.threedollar.api.bossservice.controller.store

import javax.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.bossservice.config.interceptor.Auth
import com.depromeet.threedollar.api.bossservice.config.resolver.BossId
import com.depromeet.threedollar.api.bossservice.service.store.BossStoreOpenService
import com.depromeet.threedollar.api.bossservice.service.store.BossStoreService
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.PatchBossStoreInfoRequest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.config.resolver.MapLocation
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreOpenStatusResponse
import com.depromeet.threedollar.common.model.LocationValue
import io.swagger.annotations.ApiOperation

@RestController
class BossStoreController(
    private val bossStoreOpenService: BossStoreOpenService,
    private val bossStoreService: BossStoreService,
) {

    @ApiOperation("[인증] 가게를 영업 정보를 시작/재시작 합니다", notes = "30분간 갱신하지 않으면 자동 영업 종료 처리되니 주기적으로 갱신해야 합니다.")
    @Auth
    @PostMapping("/v1/boss/store/{bossStoreId}/open")
    fun openBossStore(
        @PathVariable bossStoreId: String,
        @BossId bossId: String,
        @MapLocation mapLocation: LocationValue,
    ): ApiResponse<BossStoreOpenStatusResponse> {
        return ApiResponse.success(bossStoreOpenService.openBossStore(bossStoreId, bossId, mapLocation))
    }

    @ApiOperation("[인증] 가게를 영업 정보를 갱신합니다.", notes = "영업 중이지 않은 경우 403 에러 발생합니다 + 30분간 갱신하지 않으면 자동 영업 종료 처리되니 주기적으로 갱신해야 합니다.")
    @Auth
    @PutMapping("/v1/boss/store/{bossStoreId}/renew")
    fun patchBossStore(
        @PathVariable bossStoreId: String,
        @BossId bossId: String,
        @MapLocation mapLocation: LocationValue,
    ): ApiResponse<BossStoreOpenStatusResponse> {
        return ApiResponse.success(bossStoreOpenService.renewBossStoreOpenInfo(bossStoreId, bossId, mapLocation))
    }

    @ApiOperation("[인증] 가게를 강제로 영업 종료합니다")
    @Auth
    @DeleteMapping("/v1/boss/store/{bossStoreId}/close")
    fun closeBossStore(
        @PathVariable bossStoreId: String,
        @BossId bossId: String,
    ): ApiResponse<BossStoreOpenStatusResponse> {
        return ApiResponse.success(bossStoreOpenService.closeBossStore(bossStoreId, bossId))
    }

    @ApiOperation("[인증] 사장님 자신의 가게의 정보를 수정합니다 (전체 수정)")
    @Auth
    @PutMapping("/v1/boss/store/{bossStoreId}")
    fun updateBossStoreInfo(
        @PathVariable bossStoreId: String,
        @Valid @RequestBody request: UpdateBossStoreInfoRequest,
        @BossId bossId: String,
    ): ApiResponse<String> {
        bossStoreService.updateBossStoreInfo(bossStoreId, request, bossId)
        return ApiResponse.OK
    }

    @ApiOperation("[인증] 사장님 자신의 가게의 정보를 수정합니다 (명시한 리소스만 수정)")
    @Auth
    @PatchMapping("/v1/boss/store/{bossStoreId}")
    fun patchBossStoreInfo(
        @PathVariable bossStoreId: String,
        @Valid @RequestBody request: PatchBossStoreInfoRequest,
        @BossId bossId: String,
    ): ApiResponse<String> {
        bossStoreService.patchBossStoreInfo(bossStoreId, request, bossId)
        return ApiResponse.OK
    }

}
