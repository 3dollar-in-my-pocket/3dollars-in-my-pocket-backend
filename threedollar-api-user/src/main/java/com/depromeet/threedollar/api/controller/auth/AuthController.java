package com.depromeet.threedollar.api.controller.auth;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.service.auth.AuthService;
import com.depromeet.threedollar.api.service.auth.AuthServiceProvider;
import com.depromeet.threedollar.api.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.api.service.user.UserService;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.depromeet.threedollar.api.config.session.SessionConstants.USER_ID;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final HttpSession httpSession;
    private final UserService userService;
    private final AuthServiceProvider authServiceProvider;

    @ApiOperation("회원가입 페이지 - 회원가입을 요청합니다")
    @PostMapping("/api/v2/signup")
    public ApiResponse<LoginResponse> signUp(
        @Valid @RequestBody SignUpRequest request
    ) {
        AuthService authService = authServiceProvider.getAuthService(request.getSocialType());
        Long userId = authService.signUp(request);
        httpSession.setAttribute(USER_ID, userId);
        return ApiResponse.success(LoginResponse.of(httpSession.getId(), userId));
    }

    @ApiOperation("로그인 페이지 - 로그인을 요청합니다")
    @PostMapping("/api/v2/login")
    public ApiResponse<LoginResponse> login(
        @Valid @RequestBody LoginRequest request
    ) {
        AuthService authService = authServiceProvider.getAuthService(request.getSocialType());
        Long userId = authService.login(request);
        httpSession.setAttribute(USER_ID, userId);
        return ApiResponse.success(LoginResponse.of(httpSession.getId(), userId));
    }

    @ApiOperation("[인증] 마이페이지 - 회원탈퇴를 요청합니다")
    @Auth
    @DeleteMapping("/api/v2/signout")
    public ApiResponse<String> signOut(
        @UserId Long userId
    ) {
        userService.signOut(userId);
        httpSession.invalidate();
        return ApiResponse.SUCCESS;
    }

    @ApiOperation("[인증] 마이페이지 - 로그아웃을 요청합니다.")
    @Auth
    @PostMapping("/api/v2/logout")
    public ApiResponse<String> logout() {
        httpSession.removeAttribute(USER_ID);
        return ApiResponse.SUCCESS;
    }

}
