package com.depromeet.threedollar.api.user.controller.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.user.config.interceptor.Auth
import com.depromeet.threedollar.api.user.config.resolver.UserId
import com.depromeet.threedollar.api.user.service.user.UserActivityService
import com.depromeet.threedollar.api.user.service.user.dto.response.UserWithActivityResponse
import io.swagger.annotations.ApiOperation

@RestController
class UserActivityController(
    private val userActivityService: UserActivityService
) {

    @ApiOperation("[인증] 유저의 활동 정보를 조회합니다")
    @Auth
    @GetMapping("/v1/user/me/activity")
    fun getMyUserActivity(
        @UserId userId: Long?
    ): ApiResponse<UserWithActivityResponse> {
        return ApiResponse.success(userActivityService.getUserActivity(userId))
    }

    @Deprecated(message = "v3.0.0부터 GET /v1/user/me/activity 로 대체")
    @ApiOperation("[인증] 유저의 활동 정보를 조회합니다 (GET /api/v1/user/me/activity로 변경)")
    @Auth
    @GetMapping("/v1/user/activity")
    fun getUserActivityV1(
        @UserId userId: Long?
    ): ApiResponse<UserWithActivityResponse> {
        return ApiResponse.success(userActivityService.getUserActivity(userId))
    }

}
