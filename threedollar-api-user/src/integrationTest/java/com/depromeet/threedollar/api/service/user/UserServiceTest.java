package com.depromeet.threedollar.api.service.user;

import com.depromeet.threedollar.api.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.service.user.dto.request.CreateUserRequest;
import com.depromeet.threedollar.api.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.user.domain.user.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.depromeet.threedollar.testhelper.assertion.UserAssertionHelper.assertUser;
import static com.depromeet.threedollar.testhelper.assertion.UserAssertionHelper.assertWithdrawalUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        userRepository.deleteAllInBatch();
        withdrawalUserRepository.deleteAllInBatch();
    }

    @Nested
    class 신규_유저_생성 {

        @Test
        void 새로운_유저가_회원가입하면_새로운_데이터가_추가된다() {
            // given
            String socialId = "social-id";
            UserSocialType socialType = UserSocialType.APPLE;
            String name = "토끼";

            CreateUserRequest request = CreateUserRequest.testInstance(socialId, socialType, name);

            // when
            userService.registerUser(request);

            // then
            List<User> users = userRepository.findAll();
            assertAll(
                () -> assertThat(users).hasSize(1),
                () -> assertUser(users.get(0), socialId, socialType, name)
            );
        }

        @Test
        void 회원가입시_중복되는_닉네임인경우_Conflict_에러가_발생한다() {
            // given
            String name = "will";
            userRepository.save(UserCreator.create("social-id", UserSocialType.KAKAO, name));

            CreateUserRequest request = CreateUserRequest.testInstance("another-id", UserSocialType.APPLE, name);

            // when & then
            assertThatThrownBy(() -> userService.registerUser(request)).isInstanceOf(ConflictException.class);
        }

        @Test
        void 회원가입시_중복되는_소셜정보면_Conflict_에러가_발생한다() {
            // given
            String socialId = "social-id";
            UserSocialType socialType = UserSocialType.GOOGLE;

            userRepository.save(UserCreator.create(socialId, socialType, "기존의 닉네임"));

            CreateUserRequest request = CreateUserRequest.testInstance(socialId, socialType, "새로운 닉네임");

            // when & then
            assertThatThrownBy(() -> userService.registerUser(request)).isInstanceOf(ConflictException.class);
        }

    }

    @Nested
    class 회원_정보_조회 {

        @Test
        void 존재하지_않는_유저을_회원_조회하면_NOT_FOUND_USER_EXCEPTION() {
            // given
            Long notFoundUserId = -1L;

            // when & then
            assertThatThrownBy(() -> userService.getUserInfo(notFoundUserId)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class 중복된_닉네임_체크 {

        @Test
        void 중복된_닉네임인경우_Conflcit_에러가_발생한다() {
            // given
            String name = "토끼";
            User user = UserCreator.create("social-id", UserSocialType.KAKAO, name);
            userRepository.save(user);

            CheckAvailableNameRequest request = CheckAvailableNameRequest.testInstance(name);

            // when & then
            assertThatThrownBy(() -> userService.checkIsAvailableName(request)).isInstanceOf(ConflictException.class);
        }

        @Test
        void 중복되지_않은_닉네임이면_통과한다() {
            // given
            String name = "토끼";

            CheckAvailableNameRequest request = CheckAvailableNameRequest.testInstance(name);

            // when & then
            userService.checkIsAvailableName(request);
        }

    }

    @Nested
    class 회원정보_수정 {

        @Test
        void 나의_회원정보를_수정시_해당_회원의_데이터가_수정된다() {
            // given
            String socialId = "social-id";
            UserSocialType socialType = UserSocialType.APPLE;
            String name = "토끼";

            User user = UserCreator.create(socialId, socialType, "기존의 닉네임");
            userRepository.save(user);

            UpdateUserInfoRequest request = UpdateUserInfoRequest.testInstance(name);

            // when
            userService.updateUserInfo(request, user.getId());

            // then
            List<User> users = userRepository.findAll();
            assertAll(
                () -> assertThat(users).hasSize(1),
                () -> assertUser(users.get(0), socialId, socialType, name)
            );
        }

        @Test
        void 존재하지_않는_유저의_회원정보_수정시_NotFound_에러가_발생한다() {
            // given
            Long notFoundUserId = -1L;
            UpdateUserInfoRequest request = UpdateUserInfoRequest.testInstance("name");

            // when & then
            assertThatThrownBy(() -> userService.updateUserInfo(request, notFoundUserId)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class 회원탈퇴 {

        @Test
        void 회원탈퇴시_유저의_백업데이터가_생성된다() {
            // given
            User user = UserCreator.create("social-id", UserSocialType.KAKAO, "디프만");
            userRepository.save(user);

            // when
            userService.signOut(user.getId());

            // then
            List<User> users = userRepository.findAll();
            assertThat(users).isEmpty();

            List<WithdrawalUser> withdrawalUsers = withdrawalUserRepository.findAll();
            assertAll(
                () -> assertThat(withdrawalUsers).hasSize(1),
                () -> assertWithdrawalUser(withdrawalUsers.get(0), user)
            );
        }

        @DisplayName("회원탈퇴시 다른 유저에게 영향을 주지 않는다")
        @Test
        void 회원탈퇴시_User테이블에서의_해당_데이터는_삭제된다() {
            // given
            User user1 = UserCreator.create("social-id1", UserSocialType.KAKAO, "기존의 닉네임1");
            User user2 = UserCreator.create("social-id2", UserSocialType.APPLE, "기존의 닉네임2");

            userRepository.saveAll(List.of(user1, user2));

            // when
            userService.signOut(user1.getId());

            // then
            List<User> users = userRepository.findAll();
            assertAll(
                () -> assertThat(users).hasSize(1),
                () -> assertUser(users.get(0), user2.getSocialId(), user2.getSocialType(), user2.getName())
            );
        }

        @Test
        void 존재하지_않는_유저를_회원탈퇴시_NotFound_에러가_발생한다() {
            // given
            Long notFoundUserId = -1L;

            // when & then
            assertThatThrownBy(() -> userService.signOut(notFoundUserId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 이미_회원탈퇴한_정보가_있는_소셜정보가_있는경우라도_회원탈퇴유저_정보가_정상적으로_추가된다() {
            // given
            String socialId = "social-id";
            UserSocialType socialType = UserSocialType.GOOGLE;

            WithdrawalUser withdrawalUser = WithdrawalUserCreator.create(socialId, socialType, 10000L);
            withdrawalUserRepository.save(withdrawalUser);

            User user = UserCreator.create(socialId, socialType, "기존의 닉네임2");
            userRepository.save(user);

            // when
            userService.signOut(user.getId());

            // then
            List<WithdrawalUser> withdrawalUsers = withdrawalUserRepository.findAll();
            assertAll(
                () -> assertThat(withdrawalUsers).hasSize(2),
                () -> assertWithdrawalUser(withdrawalUsers.get(0), withdrawalUser.getUserId(), withdrawalUser.getName(), withdrawalUser.getSocialInfo()),
                () -> assertWithdrawalUser(withdrawalUsers.get(1), user.getId(), user.getName(), user.getSocialInfo())
            );
        }

    }

}
