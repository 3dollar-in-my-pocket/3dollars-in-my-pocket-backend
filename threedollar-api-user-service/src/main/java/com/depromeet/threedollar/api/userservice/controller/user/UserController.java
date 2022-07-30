package com.depromeet.threedollar.api.userservice.controller.user;

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse;
import com.depromeet.threedollar.api.userservice.config.interceptor.Auth;
import com.depromeet.threedollar.api.userservice.config.resolver.UserId;
import com.depromeet.threedollar.api.userservice.service.user.UserService;
import com.depromeet.threedollar.api.userservice.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.userservice.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.api.userservice.service.user.dto.response.UserInfoResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @ApiOperation("[인증] 나의 회원 정보를 조회합니다")
    @Auth
    @GetMapping("/v2/user/me")
    public ApiResponse<UserInfoResponse> getMyUserInfo(
        @UserId Long userId
    ) {
        return ApiResponse.success(userService.getUserInfo(userId));
    }

    @ApiOperation(value = "[인증] 나의 회원 정보를 수정합니다", notes = "중복된 닉네임 409 or 사용 불가능한 닉네임 400")
    @Auth
    @PutMapping("/v2/user/me")
    public ApiResponse<UserInfoResponse> updateMyUserInfo(
        @Valid @RequestBody UpdateUserInfoRequest request,
        @UserId Long userId
    ) {
        return ApiResponse.success(userService.updateUserInfo(request, userId));
    }

    @ApiOperation(value = "닉네임 사용 여부를 체크 요청합니다", notes = "중복된 닉네임 409 or 사용 불가능한 닉네임 400")
    @GetMapping("/v2/user/name/check")
    public ApiResponse<String> checkAvailableName(
        @Valid CheckAvailableNameRequest request
    ) {
        userService.checkIsAvailableName(request);
        return ApiResponse.OK;
    }

}
