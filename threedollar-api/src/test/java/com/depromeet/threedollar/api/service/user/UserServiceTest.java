package com.depromeet.threedollar.api.service.user;

import com.depromeet.threedollar.api.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.service.user.dto.request.CreateUserRequest;
import com.depromeet.threedollar.api.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.user.*;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WithdrawalUserRepository withdrawalUserRepository;

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        withdrawalUserRepository.deleteAll();
    }

    @Nested
    class 신규_유저_생성 {

        @Test
        void 신규_유저_생성_성공시_새로운_유저정보가_추가된다() {
            // given
            String socialId = "social-id";
            UserSocialType type = UserSocialType.KAKAO;
            String name = "가슴속 삼천원";

            CreateUserRequest request = CreateUserRequest.testInstance(socialId, type, name);

            // when
            userService.createUser(request);

            // then
            List<User> users = userRepository.findAll();
            assertThat(users).hasSize(1);
            assertUserInfo(users.get(0), socialId, type, name);
        }

        @Test
        void 신규_유저_생성시_중복된_닉네임이면_CONFLICT_EXCEPTION() {
            // given
            String name = "가슴속 삼천원";
            userRepository.save(UserCreator.create("social-id", UserSocialType.KAKAO, name));

            CreateUserRequest request = CreateUserRequest.testInstance("another-id", UserSocialType.APPLE, name);

            // when & then
            assertThatThrownBy(() -> userService.createUser(request)).isInstanceOf(ConflictException.class);
        }

        @Test
        void 신규_유저_생성시_중복된_소셜정보면_CONFLICT_EXCEPTION() {
            // given
            String socialId = "social-id";
            UserSocialType type = UserSocialType.KAKAO;
            userRepository.save(UserCreator.create(socialId, type, "기존의 닉네임"));

            CreateUserRequest request = CreateUserRequest.testInstance(socialId, type, "새로운 닉네임");

            // when & then
            assertThatThrownBy(() -> userService.createUser(request)).isInstanceOf(ConflictException.class);
        }

        @Test
        void 신규_유저_생성시_소셜_아이디가_같더라도_제공하는_소셜이_다른경우_회원가입이_정상적으로_처리된다() {
            // given
            String socialId = "social-id";
            userRepository.save(UserCreator.create(socialId, UserSocialType.APPLE, "기존의 닉네임"));

            CreateUserRequest request = CreateUserRequest.testInstance(socialId, UserSocialType.KAKAO, "새로운 닉네임");

            // when
            userService.createUser(request);

            // then
            List<User> users = userRepository.findAll();
            assertThat(users).hasSize(2);
            assertUserInfo(users.get(0), socialId, UserSocialType.APPLE, "기존의 닉네임");
            assertUserInfo(users.get(1), socialId, UserSocialType.KAKAO, "새로운 닉네임");
        }

    }

    @Nested
    class 회원_정보_조회 {

        @Test
        void 회원_정보_조회시_존재하지_않는_유저면_NOT_FOUND_USER_EXCEPTION() {
            // given
            Long userId = 100000L;

            // when & then
            assertThatThrownBy(() -> userService.getUserInfo(userId)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class 사용가능한_닉네임_체크 {

        @Test
        void 사용가능한_닉네임_체크시_중복된_닉네임이면_CONFLICT_EXCEPTION() {
            // given
            String name = "가슴속 삼천원";
            User user = UserCreator.create("social-id", UserSocialType.KAKAO, name);
            userRepository.save(user);

            CheckAvailableNameRequest request = CheckAvailableNameRequest.testInstance(name);

            // when & then
            assertThatThrownBy(() -> userService.checkAvailableName(request)).isInstanceOf(ConflictException.class);
        }

        @Test
        void 사용가능한_닉네임_체크시_중복된_닉네임이_아니면_사용가능하다() {
            // given
            String name = "가슴속 삼천원";

            CheckAvailableNameRequest request = CheckAvailableNameRequest.testInstance(name);

            // when
            userService.checkAvailableName(request);
        }

    }

    @Nested
    class 회원정보_수정 {

        @Test
        void 회원정보_수정_성공시_해당하는_유저_정보가_변경된다() {
            // given
            String socialId = "social-id";
            UserSocialType type = UserSocialType.KAKAO;
            String name = "가슴속 삼천원";
            User user = UserCreator.create(socialId, type, "기존의 닉네임");
            userRepository.save(user);

            UpdateUserInfoRequest request = UpdateUserInfoRequest.testInstance(name);

            // when
            userService.updateUserInfo(request, user.getId());

            // then
            List<User> users = userRepository.findAll();
            assertThat(users).hasSize(1);
            assertUserInfo(users.get(0), socialId, type, name);
        }

        @Test
        void 회원정보_수정_요청시_존재하지_않는_유저면_NOT_FOUND_USER_EXCEPTION() {
            // given
            Long userId = 10000000L;
            UpdateUserInfoRequest request = UpdateUserInfoRequest.testInstance("name");

            // when & then
            assertThatThrownBy(() -> userService.updateUserInfo(request, userId)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class 회원탈퇴 {

        @Test
        void 회원탈퇴_성공시_백업을_위한_데이터가_생성된다() {
            // given
            User user = UserCreator.create("social-id", UserSocialType.APPLE, "기존의 닉네임");
            userRepository.save(user);

            // then
            userService.signOut(user.getId());

            // then
            List<User> users = userRepository.findAll();
            assertThat(users).isEmpty();

            List<WithdrawalUser> withdrawalUsers = withdrawalUserRepository.findAll();
            assertThat(withdrawalUsers).hasSize(1);
            assertWithdrawalUser(withdrawalUsers.get(0), user.getId(), user.getName(), user.getSocialId(), user.getSocialType(), user.getCreatedAt());
        }

        @DisplayName("회원탈퇴시 다른 유저에게 영향을 주지 않는다")
        @Test
        void 회원탈퇴한_유저만이_USER_테이블에서_삭제된다() {
            // given
            UserSocialType type = UserSocialType.APPLE;
            User user1 = UserCreator.create("social-id1", type, "기존의 닉네임1");
            User user2 = UserCreator.create("social-id2", UserSocialType.APPLE, "기존의 닉네임2");

            userRepository.saveAll(Arrays.asList(user1, user2));

            // then
            userService.signOut(user1.getId());

            // then
            List<User> users = userRepository.findAll();
            assertThat(users).hasSize(1);
            assertUserInfo(users.get(0), user2.getSocialId(), user2.getSocialType(), user2.getName());
        }

        @Test
        void 회원탈퇴_요청시_해당하는_유저가_없으면_NOT_FOUND_USER_EXCEPTION() {
            // when & then
            assertThatThrownBy(() -> userService.signOut(1000000L)).isInstanceOf(NotFoundException.class);
        }

    }

    private void assertWithdrawalUser(WithdrawalUser withdrawalUser, Long userId, String name, String socialId, UserSocialType socialType, LocalDateTime userCreatedAt) {
        assertThat(withdrawalUser.getUserId()).isEqualTo(userId);
        assertThat(withdrawalUser.getName()).isEqualTo(name);
        assertThat(withdrawalUser.getSocialInfo().getSocialId()).isEqualTo(socialId);
        assertThat(withdrawalUser.getSocialInfo().getSocialType()).isEqualTo(socialType);
        assertThat(withdrawalUser.getUserCreatedAt()).isEqualTo(userCreatedAt);
    }

    private void assertUserInfo(User user, String socialId, UserSocialType type, String name) {
        assertThat(user.getSocialInfo().getSocialId()).isEqualTo(socialId);
        assertThat(user.getSocialInfo().getSocialType()).isEqualTo(type);
        assertThat(user.getName()).isEqualTo(name);
    }

}
