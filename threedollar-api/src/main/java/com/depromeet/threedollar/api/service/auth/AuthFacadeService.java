package com.depromeet.threedollar.api.service.auth;

import com.depromeet.threedollar.api.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class AuthFacadeService implements InitializingBean {

    private final Map<UserSocialType, AuthService> authServiceMap;

    private final AuthService appleAuthService;
    private final AuthService kaKaoAuthService;
    private final AuthService googleAuthService;

    @Override
    public void afterPropertiesSet() {
        authServiceMap.put(UserSocialType.APPLE, appleAuthService);
        authServiceMap.put(UserSocialType.GOOGLE, googleAuthService);
        authServiceMap.put(UserSocialType.KAKAO, kaKaoAuthService);
    }

    public Long signUp(SignUpRequest request) {
        AuthService authService = getAuthServiceBySocialType(request.getSocialType());
        return authService.signUp(request);
    }

    public Long login(LoginRequest request) {
        AuthService authService = getAuthServiceBySocialType(request.getSocialType());
        return authService.login(request);
    }

    private AuthService getAuthServiceBySocialType(UserSocialType socialType) {
        AuthService authService = authServiceMap.get(socialType);
        if (authService == null) {
            throw new InternalServerException(String.format("해당 소셜 타입(%s)의 AuthService를 등록해주세요.", socialType));
        }
        return authService;
    }

}
