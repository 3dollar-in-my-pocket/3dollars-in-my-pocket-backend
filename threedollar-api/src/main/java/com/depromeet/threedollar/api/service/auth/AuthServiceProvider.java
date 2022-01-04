package com.depromeet.threedollar.api.service.auth;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class AuthServiceProvider implements InitializingBean {

    private final Map<UserSocialType, AuthService> authServiceMap;

    private final AuthService appleAuthService;
    private final AuthService kaKaoAuthService;
    private final AuthService googleAuthService;

    @Override
    public void afterPropertiesSet() {
        authServiceMap.put(UserSocialType.APPLE, appleAuthService);
        authServiceMap.put(UserSocialType.GOOGLE, googleAuthService);
        authServiceMap.put(UserSocialType.KAKAO, kaKaoAuthService);
        validateInitalizeAuthServices();
    }

    private void validateInitalizeAuthServices() {
        for (UserSocialType userSocialType : UserSocialType.values()) {
            if (authServiceMap.get(userSocialType) == null) {
                throw new InternalServerException(String.format("모든 소셜에 대한 AuthService가 등록되지 않았습니다. 해당 소셜 타입(%s)의 AuthService를 등록해주세요.", userSocialType));
            }
        }
    }

    public AuthService getAuthService(UserSocialType socialType) {
        return authServiceMap.get(socialType);
    }

}
