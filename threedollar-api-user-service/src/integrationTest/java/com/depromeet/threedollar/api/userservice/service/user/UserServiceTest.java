package com.depromeet.threedollar.api.userservice.service.user;

import com.depromeet.threedollar.api.userservice.IntegrationTest;
import com.depromeet.threedollar.api.userservice.service.user.dto.request.CreateUserRequest;
import com.depromeet.threedollar.api.userservice.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.api.userservice.service.user.support.UserAssertions;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType;
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.Device;
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceFixture;
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.WithdrawalUser;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.WithdrawalUserFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.WithdrawalUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserServiceTest extends IntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WithdrawalUserRepository withdrawalUserRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Nested
    class AddUserTest {

        @Test
        void 새로운_유저가_회원가입한다() {
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
        void 회원가입시_중복된_닉네임인경우_CONFLICT_에러가_발생합니다() {
            // given
            String name = "will";
            userRepository.save(UserFixture.create("social-id", UserSocialType.KAKAO, name));

            CreateUserRequest request = CreateUserRequest.builder()
                .socialId("another-social-id")
                .socialType(UserSocialType.APPLE)
                .name(name)
                .build();

            // when & then
            assertThatThrownBy(() -> userService.registerUser(request)).isInstanceOf(ConflictException.class);
        }

        @Test
        void 회원가입시_중복된_소셜_정보인경우_CONFLICT_에러가_발생합니다() {
            // given
            String socialId = "conflict-social-id";
            UserSocialType socialType = UserSocialType.GOOGLE;

            userRepository.save(UserFixture.create(socialId, socialType, "기존의 닉네임"));

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
    class UpdateUserAccountInfoTest {

        @Test
        void 나의_유저_정보를_수정합니다() {
            // given
            String socialId = "social-id";
            UserSocialType socialType = UserSocialType.APPLE;
            String name = "토끼";

            User user = UserFixture.create(socialId, socialType, "기존의 닉네임");
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
        void 나의_유저_정보를_수정할때_존재하지_않는_유저인경우_NOTFOUND_에러가_발생합니다() {
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
            User user = UserFixture.create("social-id", UserSocialType.KAKAO, "디프만");
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
            User user1 = UserFixture.create("social-id1", UserSocialType.KAKAO, "기존의 닉네임1");
            User user2 = UserFixture.create("social-id2", UserSocialType.APPLE, "기존의 닉네임2");

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

            WithdrawalUser withdrawalUser = WithdrawalUserFixture.create(socialId, socialType, 10000L);
            withdrawalUserRepository.save(withdrawalUser);

            User user = UserFixture.create(socialId, socialType, "기존의 닉네임2");
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

        @Test
        void 회원탈퇴시_연결된_디바이스도_삭제된다() {
            // given
            User user = UserFixture.create();
            userRepository.save(user);

            Device device = DeviceFixture.create(String.valueOf(user.getId()), AccountType.USER_ACCOUNT, "pushToken");
            deviceRepository.save(device);

            // when
            userService.signOut(user.getId());

            // then
            List<Device> devices = deviceRepository.findAll();
            assertThat(devices).isEmpty();
        }

    }

}
