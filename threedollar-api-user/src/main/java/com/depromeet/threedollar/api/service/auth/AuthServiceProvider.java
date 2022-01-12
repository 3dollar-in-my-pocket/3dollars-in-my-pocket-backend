package com.depromeet.threedollar.api.service.auth;

import com.depromeet.threedollar.api.service.auth.policy.AppleAuthService;
import com.depromeet.threedollar.api.service.auth.policy.GoogleAuthService;
import com.depromeet.threedollar.api.service.auth.policy.KaKaoAuthService;
import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.domain.user.domain.user.UserSocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AuthServiceProvider {

    private static final Map<UserSocialType, AuthService> authServiceMap = new HashMap<>();

    private final AppleAuthService appleAuthService;
    private final KaKaoAuthService kaKaoAuthService;
    private final GoogleAuthService googleAuthService;

    @PostConstruct
    void initializeAuthServicesMap() {
        authServiceMap.put(UserSocialType.APPLE, appleAuthService);
        authServiceMap.put(UserSocialType.KAKAO, kaKaoAuthService);
        authServiceMap.put(UserSocialType.GOOGLE, googleAuthService);
        validateInitializeAuthServices();
    }

    private void validateInitializeAuthServices() {
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
