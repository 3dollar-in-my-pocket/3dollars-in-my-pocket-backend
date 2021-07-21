package com.depromeet.threedollar.api.controller.auth;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.common.dto.ApiResponse;
import com.depromeet.threedollar.api.service.auth.AuthService;
import com.depromeet.threedollar.api.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.service.auth.dto.request.SignOutRequest;
import com.depromeet.threedollar.api.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.api.service.token.TokenService;
import com.depromeet.threedollar.api.service.token.dto.UserTokenDto;
import com.depromeet.threedollar.common.exception.ValidationException;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService appleAuthService;
    private final AuthService kaKaoAuthService;
    private final TokenService jwtService;

    @ApiOperation("회원가입을 요청합니다")
    @PostMapping("/api/v2/signup")
    public ApiResponse<LoginResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        Long userId = signUpBySocialType(request);
        return ApiResponse.success(LoginResponse.of(jwtService.encode(new UserTokenDto(userId))));
    }

    private Long signUpBySocialType(SignUpRequest request) {
        if (request.getSocialType().equals(UserSocialType.KAKAO)) {
            return kaKaoAuthService.signUp(request);
        }
        if (request.getSocialType().equals(UserSocialType.APPLE)) {
            return appleAuthService.signUp(request);
        }
        throw new ValidationException(String.format("허용하지 않는 소셜 타입 (%s) 입니다.", request.getSocialType()));
    }

    @ApiOperation("로그인을 요청합니다")
    @PostMapping("/api/v2/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        UserTokenDto tokenDto = new UserTokenDto(loginBySocialType(request));
        return ApiResponse.success(LoginResponse.of(jwtService.encode(tokenDto)));
    }

    private Long loginBySocialType(LoginRequest request) {
        if (request.getSocialType().equals(UserSocialType.KAKAO)) {
            return kaKaoAuthService.login(request);
        }
        if (request.getSocialType().equals(UserSocialType.APPLE)) {
            return appleAuthService.login(request);
        }
        throw new ValidationException(String.format("허용하지 않는 소셜 타입 (%s) 입니다.", request.getSocialType()));
    }

    @ApiOperation("[인증] 회원탈퇴를 요청합니다")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @DeleteMapping("/api/v2/signout")
    public ApiResponse<String> signOut(@Valid SignOutRequest request, @UserId Long userId) {
        signOutBySocialType(request, userId);
        return ApiResponse.SUCCESS;
    }

    private void signOutBySocialType(SignOutRequest request, Long userId) {
        if (request.getSocialType().equals(UserSocialType.KAKAO)) {
            kaKaoAuthService.signOut(userId);
        }
        if (request.getSocialType().equals(UserSocialType.APPLE)) {
            appleAuthService.signOut(userId);
        }
        throw new ValidationException(String.format("허용하지 않는 소셜 타입 (%s) 입니다.", request.getSocialType()));
    }

}
