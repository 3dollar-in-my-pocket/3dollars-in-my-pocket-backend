package com.depromeet.threedollar.api.userservice.service.auth;

import com.depromeet.threedollar.api.userservice.IntegrationTest;
import com.depromeet.threedollar.api.userservice.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.userservice.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.userservice.service.user.UserService;
import com.depromeet.threedollar.api.userservice.service.user.support.UserAssertions;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import com.depromeet.threedollar.infrastructure.external.client.apple.AppleTokenDecoder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class AppleAuthServiceTest extends IntegrationTest {

    private static final String SOCIAL_ID = "apple-social-id";
    private static final UserSocialType SOCIAL_TYPE = UserSocialType.APPLE;

    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        authService = new AppleAuthService(new StubAppleTokenDecoder(), userRepository, userService);
    }

    private static class StubAppleTokenDecoder implements AppleTokenDecoder {

        @Override
        public String getSocialIdFromIdToken(@NotNull String idToken) {
            return SOCIAL_ID;
        }

    }

    @Nested
    class AppleLoginTest {

        @Test
        void 애플_로그인_성공시_멤버의_ID_가_반환된다() {
            // given
            User user = UserFixture.create(SOCIAL_ID, SOCIAL_TYPE, "닉네임");
            userRepository.save(user);

            LoginRequest request = LoginRequest.testBuilder()
                .token("social-access-token")
                .socialType(SOCIAL_TYPE)
                .build();

            // when
            Long userId = authService.login(request);

            // then
            assertThat(userId).isEqualTo(user.getId());
        }

        @Test
        void 애플_로그인시_가입한_유저가_아니면_NotFound_에러가_발생한다() {
            // given
            LoginRequest request = LoginRequest.testBuilder()
                .token("social-access-token")
                .socialType(SOCIAL_TYPE)
                .build();

            // when & then
            assertThatThrownBy(() -> authService.login(request)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class AppleSignupTest {

        @Test
        void 애플_회원가입시_새로운_유저가_등록된다() {
            // given
            SignUpRequest request = SignUpRequest.testBuilder()
                .token("social-access-token")
                .name("가슴속 삼천원")
                .socialType(SOCIAL_TYPE)
                .build();

            // when
            authService.signUp(request);

            // then
            List<User> users = userRepository.findAll();
            assertAll(
                () -> assertThat(users).hasSize(1),
                () -> UserAssertions.assertUser(users.get(0), SOCIAL_ID, request.getSocialType(), request.getName())
            );
        }

        @Test
        void 애플_회원가입시_이미_가입한_유저면_Conflict_에러_발생() {
            // given
            userRepository.save(UserFixture.create(SOCIAL_ID, SOCIAL_TYPE, "헬로우"));

            SignUpRequest request = SignUpRequest.testBuilder()
                .token("social-access-token")
                .name("가슴속 삼천원")
                .socialType(SOCIAL_TYPE)
                .build();

            // when & then
            assertThatThrownBy(() -> authService.signUp(request)).isInstanceOf(ConflictException.class);
        }

    }

}
