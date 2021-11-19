package com.depromeet.threedollar.api.service.auth;

import com.depromeet.threedollar.api.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.common.exception.model.ValidationException;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.depromeet.threedollar.common.exception.ErrorCode.VALIDATION_SOCIAL_TYPE_EXCEPTION;

@RequiredArgsConstructor
@Service
public class AuthAssembleService {

    private final AuthService appleAuthService;
    private final AuthService kaKaoAuthService;
    private final AuthService googleAuthService;

    public Long signUp(SignUpRequest request) {
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

    public Long login(LoginRequest request) {
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

}
