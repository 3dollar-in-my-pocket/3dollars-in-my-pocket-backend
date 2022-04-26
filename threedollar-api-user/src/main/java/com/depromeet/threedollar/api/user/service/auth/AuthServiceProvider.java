package com.depromeet.threedollar.api.user.service.auth;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;

@RequiredArgsConstructor
@Component
public class AuthServiceProvider {

    private static final EnumMap<UserSocialType, AuthService> authServiceMap = new EnumMap<>(UserSocialType.class);

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
                throw new InternalServerException(String.format("예상치 못한 에러가 발생하였습니다. 소셜 타입(%s)에 대한 AuthService를 등록해주세요.", userSocialType));
            }
        }
    }

    public AuthService getAuthService(UserSocialType socialType) {
        return authServiceMap.get(socialType);
    }

}
