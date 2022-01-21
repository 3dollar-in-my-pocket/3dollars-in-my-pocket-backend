package com.depromeet.threedollar.api.service.auth;

import com.depromeet.threedollar.api.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.service.auth.policy.GoogleAuthService;
import com.depromeet.threedollar.api.service.user.UserService;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.user.domain.user.*;
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient;
import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.depromeet.threedollar.api.assertutils.assertUserUtils.assertUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class GoogleAuthServiceTest {

    private static final String SOCIAL_ID = "google-social-id";
    private static final UserSocialType SOCIAL_TYPE = UserSocialType.GOOGLE;

    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WithdrawalUserRepository withdrawalUserRepository;

    @BeforeEach
    void setup() {
        authService = new GoogleAuthService(new StubGoogleAuthApiClient(), userService, userRepository);
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        withdrawalUserRepository.deleteAllInBatch();
    }

    @Nested
    class 구글_로그인 {

        @Test
        void 구글_로그인_성공시_ID가_반환된다() {
            // given
            User user = UserCreator.create(SOCIAL_ID, SOCIAL_TYPE, "닉네임");
            userRepository.save(user);

            LoginRequest request = LoginRequest.testInstance("token", SOCIAL_TYPE);

            // when
            Long userId = authService.login(request);

            // then
            assertThat(userId).isEqualTo(user.getId());
        }

        @Test
        void 구글_로그인시_가입한_유저가_아니면_NotFound_에러가_발생한다() {
            // given
            LoginRequest request = LoginRequest.testInstance("token", SOCIAL_TYPE);

            // when & then
            assertThatThrownBy(() -> authService.login(request)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class 구글_회원가입 {

        @Test
        void 구글_회원가입_성공시_새로운_유저정보가_저장된다() {
            // given
            SignUpRequest request = SignUpRequest.testInstance("token", "가슴속 삼천원", SOCIAL_TYPE);

            // when
            authService.signUp(request);

            // then
            List<User> users = userRepository.findAll();
            assertThat(users).hasSize(1);
            assertUser(users.get(0), SOCIAL_ID, request.getSocialType(), request.getName());
        }

        @Test
        void 구글_회원가입시_이미_가입한_유저면_Conflict_에러_발생() {
            // given
            userRepository.save(UserCreator.create(SOCIAL_ID, SOCIAL_TYPE, "헬로우"));

            SignUpRequest request = SignUpRequest.testInstance("token", "가슴속 삼천원", SOCIAL_TYPE);

            // when & then
            assertThatThrownBy(() -> authService.signUp(request)).isInstanceOf(ConflictException.class);
        }

    }

    private static class StubGoogleAuthApiClient implements GoogleAuthApiClient {

        @Override
        public GoogleProfileInfoResponse getProfileInfo(String accessToken) {
            return GoogleProfileInfoResponse.testInstance(SOCIAL_ID);
        }

    }

}
