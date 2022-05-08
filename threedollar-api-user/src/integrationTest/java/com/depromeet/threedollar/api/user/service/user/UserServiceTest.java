package com.depromeet.threedollar.api.user.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.depromeet.threedollar.api.user.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.user.service.user.dto.request.CreateUserRequest;
import com.depromeet.threedollar.api.user.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.api.user.service.user.support.UserAssertions;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.user.domain.user.User;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserCreator;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserRepository;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType;
import com.depromeet.threedollar.domain.rds.user.domain.user.WithdrawalUser;
import com.depromeet.threedollar.domain.rds.user.domain.user.WithdrawalUserCreator;
import com.depromeet.threedollar.domain.rds.user.domain.user.WithdrawalUserRepository;

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
    class AddUserTest {

        @Test
        void 새로운_유저가_회원가입하면_새로운_데이터가_추가된다() {
            // given
            String socialId = "user-social-id";
            UserSocialType socialType = UserSocialType.APPLE;
            String name = "토끼";

            CreateUserRequest request = CreateUserRequest.builder()
                .socialId(socialId)
                .socialType(socialType)
                .name(name)
                .build();

            // when
            userService.registerUser(request);

            // then
            List<User> users = userRepository.findAll();
            assertAll(
                () -> assertThat(users).hasSize(1),
                () -> UserAssertions.assertUser(users.get(0), socialId, socialType, name)
            );
        }

        @Test
        void 회원가입시_중복되는_닉네임인경우_Conflict_에러가_발생한다() {
            // given
            String name = "will";
            userRepository.save(UserCreator.builder()
                .socialId("social-id")
                .socialType(UserSocialType.KAKAO)
                .name(name)
                .build());

            CreateUserRequest request = CreateUserRequest.builder()
                .socialId("another-social-id")
                .socialType(UserSocialType.APPLE)
                .name(name)
                .build();

            // when & then
            assertThatThrownBy(() -> userService.registerUser(request)).isInstanceOf(ConflictException.class);
        }

        @Test
        void 회원가입시_중복되는_소셜정보면_Conflict_에러가_발생한다() {
            // given
            String socialId = "conflict-social-id";
            UserSocialType socialType = UserSocialType.GOOGLE;

            userRepository.save(UserCreator.builder()
                .socialId(socialId)
                .socialType(socialType)
                .name("기존의 닉네임")
                .build());

            CreateUserRequest request = CreateUserRequest.builder()
                .socialId(socialId)
                .socialType(socialType)
                .name("새로운 닉네임")
                .build();

            // when & then
            assertThatThrownBy(() -> userService.registerUser(request)).isInstanceOf(ConflictException.class);
        }

    }

    @Nested
    class GetUserAccountInfoTest {

        @Test
        void 존재하지_않는_유저을_회원_조회하면_NOT_FOUND_USER_EXCEPTION() {
            // given
            Long notFoundUserId = -1L;

            // when & then
            assertThatThrownBy(() -> userService.getUserInfo(notFoundUserId)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class CheckDuplicateNicknameTest {

        @Test
        void 중복된_닉네임인경우_Conflcit_에러가_발생한다() {
            // given
            String name = "토끼";
            userRepository.save(UserCreator.builder()
                .socialId("social-id")
                .socialType(UserSocialType.KAKAO)
                .name(name)
                .build());

            CheckAvailableNameRequest request = CheckAvailableNameRequest.testBuilder()
                .name(name)
                .build();

            // when & then
            assertThatThrownBy(() -> userService.checkIsAvailableName(request)).isInstanceOf(ConflictException.class);
        }

        @Test
        void 중복되지_않은_닉네임이면_통과한다() {
            // given
            CheckAvailableNameRequest request = CheckAvailableNameRequest.testBuilder()
                .name("토끼")
                .build();

            // when & then
            assertDoesNotThrow(() -> userService.checkIsAvailableName(request));
        }

    }

    @Nested
    class UpdateUserAccountInfoTest {

        @Test
        void 나의_회원정보를_수정시_해당_회원의_데이터가_수정된다() {
            // given
            String socialId = "social-id";
            UserSocialType socialType = UserSocialType.APPLE;
            String name = "토끼";

            User user = UserCreator.builder()
                .socialId(socialId)
                .socialType(socialType)
                .name("기존의 닉네임")
                .build();
            userRepository.save(user);

            UpdateUserInfoRequest request = UpdateUserInfoRequest.testBuilder()
                .name(name)
                .build();

            // when
            userService.updateUserInfo(request, user.getId());

            // then
            List<User> users = userRepository.findAll();
            assertAll(
                () -> assertThat(users).hasSize(1),
                () -> UserAssertions.assertUser(users.get(0), socialId, socialType, name)
            );
        }

        @Test
        void 존재하지_않는_유저의_회원정보_수정시_NotFound_에러가_발생한다() {
            // given
            Long notFoundUserId = -1L;

            UpdateUserInfoRequest request = UpdateUserInfoRequest.testBuilder()
                .name("name")
                .build();

            // when & then
            assertThatThrownBy(() -> userService.updateUserInfo(request, notFoundUserId)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class SignOutTest {

        @Test
        void 회원탈퇴시_유저의_백업데이터가_생성된다() {
            // given
            User user = UserCreator.builder()
                .socialId("social-id")
                .socialType(UserSocialType.KAKAO)
                .name("디프만")
                .build();
            userRepository.save(user);

            // when
            userService.signOut(user.getId());

            // then
            List<User> users = userRepository.findAll();
            assertThat(users).isEmpty();

            List<WithdrawalUser> withdrawalUsers = withdrawalUserRepository.findAll();
            assertAll(
                () -> assertThat(withdrawalUsers).hasSize(1),
                () -> UserAssertions.assertWithdrawalUser(withdrawalUsers.get(0), user)
            );
        }

        @DisplayName("회원탈퇴시 다른 유저에게 영향을 주지 않는다")
        @Test
        void 회원탈퇴시_User테이블에서의_해당_데이터는_삭제된다() {
            // given
            User user1 = UserCreator.builder()
                .socialId("social-id1")
                .socialType(UserSocialType.KAKAO)
                .name("닉네임1")
                .build();

            User user2 = UserCreator.builder()
                .socialId("social-id")
                .socialType(UserSocialType.APPLE)
                .name("닉네임2")
                .build();

            userRepository.saveAll(List.of(user1, user2));

            // when
            userService.signOut(user1.getId());

            // then
            List<User> users = userRepository.findAll();
            assertAll(
                () -> assertThat(users).hasSize(1),
                () -> UserAssertions.assertUser(users.get(0), user2.getSocialId(), user2.getSocialType(), user2.getName())
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

            WithdrawalUser withdrawalUser = WithdrawalUserCreator.builder()
                .socialId(socialId)
                .socialType(socialType)
                .userId(10000L)
                .build();
            withdrawalUserRepository.save(withdrawalUser);

            User user = UserCreator.builder()
                .socialId(socialId)
                .socialType(socialType)
                .name("기존의 닉네임")
                .build();
            userRepository.save(user);

            // when
            userService.signOut(user.getId());

            // then
            List<WithdrawalUser> withdrawalUsers = withdrawalUserRepository.findAll();
            assertAll(
                () -> assertThat(withdrawalUsers).hasSize(2),
                () -> UserAssertions.assertWithdrawalUser(withdrawalUsers.get(0), withdrawalUser.getUserId(), withdrawalUser.getName(), withdrawalUser.getSocialInfo()),
                () -> UserAssertions.assertWithdrawalUser(withdrawalUsers.get(1), user.getId(), user.getName(), user.getSocialInfo())
            );
        }

    }

}
