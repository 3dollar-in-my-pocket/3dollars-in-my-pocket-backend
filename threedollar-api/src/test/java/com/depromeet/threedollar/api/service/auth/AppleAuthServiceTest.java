package com.depromeet.threedollar.api.service.auth;

import com.depromeet.threedollar.api.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.service.user.UserService;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.user.*;
import com.depromeet.threedollar.external.client.apple.AppleTokenProvider;
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
class AppleAuthServiceTest {

    private static final String socialId = "social-id";
    private static final UserSocialType socialType = UserSocialType.APPLE;

    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WithdrawalUserRepository withdrawalUserRepository;

    @BeforeEach
    void setUp() {
        authService = new AppleAuthService(new StubAppleTokenProvider(), userRepository, userService);
    }

    @AfterEach
    void cleanUp() {
        withdrawalUserRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    class 애플_로그인 {

        @Test
        void 애플_로그인_성공시_멤버의_ID_가_반환된다() {
            // given
            User user = UserCreator.create(socialId, socialType, "닉네임");
            userRepository.save(user);

            LoginRequest request = LoginRequest.testInstance("token", socialType);

            // when
            Long userId = authService.login(request);

            // then
            assertThat(userId).isEqualTo(user.getId());
        }

        @Test
        void 애플_로그인시_가입한_유저가_아니면_NotFound_에러가_발생한다() {
            // given
            LoginRequest request = LoginRequest.testInstance("token", socialType);

            // when & then
            assertThatThrownBy(() -> authService.login(request)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class 애플_회원가입 {

        @Test
        void 애플_회원가입시_새로운_유저가_등록된다() {
            // given
            SignUpRequest request = SignUpRequest.testInstance("token", "가슴속 삼천원", socialType);

            authService.signUp(request);

            // then
            List<User> users = userRepository.findAll();
            assertThat(users).hasSize(1);
            assertUser(users.get(0), socialId, request.getSocialType(), request.getName());
        }

        @Test
        void 애플_회원가입시_이미_가입한_유저면_Conflict_에러_발생() {
            // given
            userRepository.save(UserCreator.create(socialId, socialType, "헬로우"));

            SignUpRequest request = SignUpRequest.testInstance("token", "가슴속 삼천원", socialType);

            // when & then
            assertThatThrownBy(() -> authService.signUp(request)).isInstanceOf(ConflictException.class);
        }

    }

    private static class StubAppleTokenProvider implements AppleTokenProvider {

        @Override
        public String getSocialIdFromIdToken(String idToken) {
            return socialId;
        }

    }

}
