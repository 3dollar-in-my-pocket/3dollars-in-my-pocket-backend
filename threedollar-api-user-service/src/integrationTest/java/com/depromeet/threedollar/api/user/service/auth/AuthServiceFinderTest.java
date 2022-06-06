package com.depromeet.threedollar.api.user.service.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;

@SpringBootTest
class AuthServiceFinderTest {

    @Autowired
    private AuthServiceFinder authServiceFinder;

    @MethodSource("userSocialTypeAndServices")
    @ParameterizedTest
    void 각_소셜의_AuthService_구현체를_가져옵니다(UserSocialType socialType, Class<AuthService> expectedAuthServiceClass) {
        // when
        AuthService authService = authServiceFinder.getAuthService(socialType);

        // then
        assertThat(authService).isInstanceOf(expectedAuthServiceClass);
    }

    private static Stream<Arguments> userSocialTypeAndServices() {
        return Stream.of(
            Arguments.of(UserSocialType.KAKAO, KaKaoAuthService.class),
            Arguments.of(UserSocialType.APPLE, AppleAuthService.class),
            Arguments.of(UserSocialType.GOOGLE, GoogleAuthService.class)
        );
    }

}
