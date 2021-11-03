package com.depromeet.threedollar.api.controller.auth;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.service.user.UserService;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.api.service.auth.AuthService;
import com.depromeet.threedollar.api.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.common.exception.model.ValidationException;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.depromeet.threedollar.api.config.session.SessionConstants.USER_ID;
import static com.depromeet.threedollar.common.exception.ErrorCode.VALIDATION_SOCIAL_TYPE_EXCEPTION;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService appleAuthService;
    private final AuthService kaKaoAuthService;
    private final AuthService googleAuthService;
    private final HttpSession httpSession;
    private final UserService userService;

    @ApiOperation("회원가입 페이지 - 회원가입을 요청합니다")
    @PostMapping("/api/v2/signup")
    public ApiResponse<LoginResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        Long userId = signUpBySocialType(request);
        httpSession.setAttribute(USER_ID, userId);
        return ApiResponse.success(LoginResponse.of(httpSession.getId(), userId));
    }

    private Long signUpBySocialType(SignUpRequest request) {
        if (request.getSocialType().equals(UserSocialType.KAKAO)) {
            return kaKaoAuthService.signUp(request);
        }
        if (request.getSocialType().equals(UserSocialType.APPLE)) {
            return appleAuthService.signUp(request);
        }
        if (request.getSocialType().equals(UserSocialType.GOOGLE)) {
            return googleAuthService.signUp(request);
        }
        throw new ValidationException(String.format("허용하지 않는 소셜 타입 (%s) 입니다.", request.getSocialType()), VALIDATION_SOCIAL_TYPE_EXCEPTION);
    }

    @ApiOperation("로그인 페이지 - 로그인을 요청합니다")
    @PostMapping("/api/v2/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Long userId = loginBySocialType(request);
        httpSession.setAttribute(USER_ID, userId);
        return ApiResponse.success(LoginResponse.of(httpSession.getId(), userId));
    }

    private Long loginBySocialType(LoginRequest request) {
        if (request.getSocialType().equals(UserSocialType.KAKAO)) {
            return kaKaoAuthService.login(request);
        }
        if (request.getSocialType().equals(UserSocialType.APPLE)) {
            return appleAuthService.login(request);
        }
        if (request.getSocialType().equals(UserSocialType.GOOGLE)) {
            return googleAuthService.login(request);
        }
        throw new ValidationException(String.format("허용하지 않는 소셜 타입 (%s) 입니다.", request.getSocialType()), VALIDATION_SOCIAL_TYPE_EXCEPTION);
    }

    @ApiOperation("[인증] 마이페이지 - 회원탈퇴를 요청합니다")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @DeleteMapping("/api/v2/signout")
    public ApiResponse<String> signOut(@UserId Long userId) {
        userService.signOut(userId);
        httpSession.invalidate();
        return ApiResponse.SUCCESS;
    }

    @ApiOperation("[인증] 마이페이지 - 로그아웃을 요청합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @PostMapping("/api/v2/logout")
    public ApiResponse<String> logout() {
        httpSession.removeAttribute(USER_ID);
        return ApiResponse.SUCCESS;
    }

}
