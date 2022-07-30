package com.depromeet.threedollar.api.userservice.controller.auth;

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse;
import com.depromeet.threedollar.api.userservice.config.interceptor.Auth;
import com.depromeet.threedollar.api.userservice.config.resolver.UserId;
import com.depromeet.threedollar.api.userservice.service.auth.AuthService;
import com.depromeet.threedollar.api.userservice.service.auth.AuthServiceFinder;
import com.depromeet.threedollar.api.userservice.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.userservice.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.userservice.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.api.userservice.service.user.UserService;
import com.depromeet.threedollar.domain.rds.event.userservice.user.UserLogOutedEvent;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.depromeet.threedollar.api.userservice.config.session.SessionConstants.USER_ID;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final HttpSession httpSession;
    private final UserService userService;
    private final AuthServiceFinder authServiceFinder;
    private final ApplicationEventPublisher eventPublisher;

    @ApiOperation("회원가입을 요청합니다")
    @PostMapping("/v2/signup")
    public ApiResponse<LoginResponse> signUp(
        @Valid @RequestBody SignUpRequest request
    ) {
        AuthService authService = authServiceFinder.getAuthService(request.getSocialType());
        Long userId = authService.signUp(request);
        httpSession.setAttribute(USER_ID, userId);
        return ApiResponse.success(LoginResponse.of(httpSession.getId(), userId));
    }

    @ApiOperation("로그인을 요청합니다")
    @PostMapping("/v2/login")
    public ApiResponse<LoginResponse> login(
        @Valid @RequestBody LoginRequest request
    ) {
        AuthService authService = authServiceFinder.getAuthService(request.getSocialType());
        Long userId = authService.login(request);
        httpSession.setAttribute(USER_ID, userId);
        return ApiResponse.success(LoginResponse.of(httpSession.getId(), userId));
    }

    @ApiOperation("[인증] 회원탈퇴를 요청합니다")
    @Auth
    @DeleteMapping("/v2/signout")
    public ApiResponse<String> signOut(
        @UserId Long userId
    ) {
        userService.signOut(userId);
        httpSession.invalidate();
        return ApiResponse.OK;
    }

    @ApiOperation("[인증] 로그아웃을 요청합니다.")
    @Auth
    @PostMapping("/v2/logout")
    public ApiResponse<String> logout(
        @UserId Long userId
    ) {
        eventPublisher.publishEvent(UserLogOutedEvent.of(userId));
        httpSession.invalidate();
        return ApiResponse.OK;
    }

}
