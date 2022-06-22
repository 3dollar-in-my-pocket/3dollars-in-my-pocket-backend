package com.depromeet.threedollar.api.bossservice.controller.store

import javax.validation.Valid
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.bossservice.config.interceptor.Auth
import com.depromeet.threedollar.api.bossservice.config.resolver.BossId
import com.depromeet.threedollar.api.bossservice.service.store.BossStoreService
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.PatchBossStoreInfoRequest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation

@RestController
class BossStoreController(
    private val bossStoreService: BossStoreService,
) {

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
