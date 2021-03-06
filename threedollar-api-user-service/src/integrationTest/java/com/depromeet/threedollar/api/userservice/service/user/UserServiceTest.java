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
        void ?????????_?????????_??????????????????() {
            // given
            String socialId = "user-social-id";
            UserSocialType socialType = UserSocialType.APPLE;
            String name = "??????";

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
        void ???????????????_?????????_??????????????????_CONFLICT_?????????_???????????????() {
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
        void ???????????????_?????????_??????_???????????????_CONFLICT_?????????_???????????????() {
            // given
            String socialId = "conflict-social-id";
            UserSocialType socialType = UserSocialType.GOOGLE;

            userRepository.save(UserFixture.create(socialId, socialType, "????????? ?????????"));

            CreateUserRequest request = CreateUserRequest.builder()
                .socialId(socialId)
                .socialType(socialType)
                .name("????????? ?????????")
                .build();

            // when & then
            assertThatThrownBy(() -> userService.registerUser(request)).isInstanceOf(ConflictException.class);
        }

    }

    @Nested
    class UpdateUserAccountInfoTest {

        @Test
        void ??????_??????_?????????_???????????????() {
            // given
            String socialId = "social-id";
            UserSocialType socialType = UserSocialType.APPLE;
            String name = "??????";

            User user = UserFixture.create(socialId, socialType, "????????? ?????????");
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
        void ??????_??????_?????????_????????????_????????????_??????_???????????????_NOTFOUND_?????????_???????????????() {
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
        void ???????????????_?????????_??????????????????_????????????() {
            // given
            User user = UserFixture.create("social-id", UserSocialType.KAKAO, "?????????");
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

        @DisplayName("??????????????? ?????? ???????????? ????????? ?????? ?????????")
        @Test
        void ???????????????_User??????????????????_??????_????????????_????????????() {
            // given
            User user1 = UserFixture.create("social-id1", UserSocialType.KAKAO, "????????? ?????????1");
            User user2 = UserFixture.create("social-id2", UserSocialType.APPLE, "????????? ?????????2");

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
        void ????????????_??????_?????????_???????????????_NotFound_?????????_????????????() {
            // given
            Long notFoundUserId = -1L;

            // when & then
            assertThatThrownBy(() -> userService.signOut(notFoundUserId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void ??????_???????????????_?????????_??????_???????????????_??????????????????_??????????????????_?????????_???????????????_????????????() {
            // given
            String socialId = "social-id";
            UserSocialType socialType = UserSocialType.GOOGLE;

            WithdrawalUser withdrawalUser = WithdrawalUserFixture.create(socialId, socialType, 10000L);
            withdrawalUserRepository.save(withdrawalUser);

            User user = UserFixture.create(socialId, socialType, "????????? ?????????2");
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
        void ???????????????_?????????_???????????????_????????????() {
            // given
            User user = UserFixture.create();
            userRepository.save(user);

            Device device = DeviceFixture.create(String.valueOf(user.getId()), AccountType.USER_ACCOUNT);
            deviceRepository.save(device);

            // when
            userService.signOut(user.getId());

            // then
            List<Device> devices = deviceRepository.findAll();
            assertThat(devices).isEmpty();
        }

    }

}
