package com.depromeet.threedollar.api.controller.user;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.service.user.UserService;
import com.depromeet.threedollar.api.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
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

    @ApiOperation("[인증] 마이페이지 - 나의 회원 정보를 조회합니다")
    @Auth
    @GetMapping("/api/v2/user/me")
    public ApiResponse<UserInfoResponse> getMyUserInfo(
        @UserId Long userId
    ) {
        return ApiResponse.success(userService.getUserInfo(userId));
    }

    @ApiOperation("[인증] 마이페이지 - 나의 회원 정보를 수정합니다")
    @Auth
    @PutMapping("/api/v2/user/me")
    public ApiResponse<UserInfoResponse> updateMyUserInfo(
        @Valid @RequestBody UpdateUserInfoRequest request,
        @UserId Long userId
    ) {
        return ApiResponse.success(userService.updateUserInfo(request, userId));
    }

    @ApiOperation("회원가입 & 마이페이지 - 닉네임 사용 여부를 체크 요청합니다. (중복된 닉네임 409 or 사용 불가능한 닉네임 400)")
    @GetMapping("/api/v2/user/name/check")
    public ApiResponse<String> checkAvailableName(
        @Valid CheckAvailableNameRequest request
    ) {
        userService.checkIsAvailableName(request);
        return ApiResponse.SUCCESS;
    }


}
