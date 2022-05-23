package com.depromeet.threedollar.api.vendor.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.depromeet.threedollar.api.vendor.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.vendor.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.vendor.service.user.UserService;
import com.depromeet.threedollar.api.vendor.service.user.support.UserAssertions;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.vendor.domain.user.User;
import com.depromeet.threedollar.domain.rds.vendor.domain.user.UserCreator;
import com.depromeet.threedollar.domain.rds.vendor.domain.user.UserRepository;
import com.depromeet.threedollar.domain.rds.vendor.domain.user.UserSocialType;
import com.depromeet.threedollar.domain.rds.vendor.domain.user.WithdrawalUserRepository;
import com.depromeet.threedollar.external.client.kakao.KaKaoAuthApiClient;
import com.depromeet.threedollar.external.client.kakao.dto.response.KaKaoProfileResponse;

@SpringBootTest
class KaKaoAuthServiceTest {

    private static final String SOCIAL_ID = "social-id";
    private static final UserSocialType SOCIAL_TYPE = UserSocialType.KAKAO;

    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WithdrawalUserRepository withdrawalUserRepository;

    @BeforeEach
    void setUp() {
        authService = new KaKaoAuthService(new StubKaKaoAuthApiCaller(), userService, userRepository);
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        withdrawalUserRepository.deleteAllInBatch();
    }

    private static class StubKaKaoAuthApiCaller implements KaKaoAuthApiClient {

        @Override
        public KaKaoProfileResponse getProfileInfo(String accessToken) {
            return KaKaoProfileResponse.testInstance(SOCIAL_ID);
        }

    }

    @Nested
    class KaKaoLoginTest {

        @Test
        void 카카오_로그인_성공시_멤버의_ID_가_반환된다() {
            // given
            User user = UserCreator.create(SOCIAL_ID, SOCIAL_TYPE, "닉네임");
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
        void 카카오_로그인시_가입한_유저가_아니면_NotFound_에러가_발생한다() {
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
    class KaKaoSignUpTest {

        @Test
        void 카카오_회원가입시_새로운_유저가_등록된다() {
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
        void 카카오_회원가입시_이미_가입한_유저면_Conflict_에러_발생() {
            // given
            userRepository.save(UserCreator.create(SOCIAL_ID, SOCIAL_TYPE, "헬로우"));

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
