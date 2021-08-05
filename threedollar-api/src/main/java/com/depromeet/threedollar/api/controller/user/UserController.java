package com.depromeet.threedollar.api.controller.user;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.common.dto.ApiResponse;
import com.depromeet.threedollar.api.service.user.UserService;
import com.depromeet.threedollar.api.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @ApiOperation("[인증] 나의 회원 정보를 조회합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @GetMapping("/api/v2/user/me")
    public ApiResponse<UserInfoResponse> getMyUserInfo(@UserId Long userId) {
        return ApiResponse.success(userService.getUserInfo(userId));
    }

    @ApiOperation("[인증] 나의 회원 정보를 수정합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @PutMapping("/api/v2/user/me")
    public ApiResponse<UserInfoResponse> updateMyUserInfo(@Valid @RequestBody UpdateUserInfoRequest request,
                                                          @UserId Long userId) {
        return ApiResponse.success(userService.updateUserInfo(request, userId));
    }

    @ApiOperation("닉네임 중복 여부를 체크 요청합니다.")
    @GetMapping("/api/v2/user/name/check")
    public ApiResponse<String> checkAvailableName(@Valid CheckAvailableNameRequest request) {
        userService.checkAvailableName(request);
        return ApiResponse.SUCCESS;
    }

}
