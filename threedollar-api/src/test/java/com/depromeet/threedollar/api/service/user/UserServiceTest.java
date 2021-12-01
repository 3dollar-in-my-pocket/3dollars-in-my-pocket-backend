package com.depromeet.threedollar.api.service.user;

import com.depromeet.threedollar.api.service.medal.dto.request.ActivateUserMedalRequest;
import com.depromeet.threedollar.api.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.service.user.dto.request.CreateUserRequest;
import com.depromeet.threedollar.api.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.medal.*;
import com.depromeet.threedollar.domain.domain.user.*;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.depromeet.threedollar.api.assertutils.assertUserUtils.*;
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

    @Autowired
    private MedalRepository medalRepository;

    @Autowired
    private UserMedalRepository userMedalRepository;

    @AfterEach
    void cleanUp() {
        userMedalRepository.deleteAllInBatch();
        medalRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        withdrawalUserRepository.deleteAll();
    }

    @Nested
    class 신규_유저_생성 {

        @AutoSource
        @ParameterizedTest
        void 새로운_유저가_회원가입하면_새로운_데이터가_추가된다(String socialId, UserSocialType socialType, String name) {
            // given
            CreateUserRequest request = CreateUserRequest.testInstance(socialId, socialType, name);

            // when
            userService.createUser(request);

            // then
            List<User> users = userRepository.findAll();
            assertThat(users).hasSize(1);
            assertUser(users.get(0), socialId, socialType, name);
        }

        @AutoSource
        @ParameterizedTest
        void 회원가입시_중복되는_닉네임인경우_Conflict_에러가_발생한다(String name) {
            // given
            userRepository.save(UserCreator.create("social-id", UserSocialType.KAKAO, name));

            CreateUserRequest request = CreateUserRequest.testInstance("another-id", UserSocialType.APPLE, name);

            // when & then
            assertThatThrownBy(() -> userService.createUser(request)).isInstanceOf(ConflictException.class);
        }

        @AutoSource
        @ParameterizedTest
        void 회원가입시_중복되는_소셜정보면_Conflict_에러가_발생한다(String socialId, UserSocialType socialType) {
            // given
            userRepository.save(UserCreator.create(socialId, socialType, "기존의 닉네임"));

            CreateUserRequest request = CreateUserRequest.testInstance(socialId, socialType, "새로운 닉네임");

            // when & then
            assertThatThrownBy(() -> userService.createUser(request)).isInstanceOf(ConflictException.class);
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

        @AutoSource
        @ParameterizedTest
        void 중복된_닉네임인경우_Conflcit_에러가_발생한다(String name) {
            // given
            User user = UserCreator.create("social-id", UserSocialType.KAKAO, name);
            userRepository.save(user);

            CheckAvailableNameRequest request = CheckAvailableNameRequest.testInstance(name);

            // when & then
            assertThatThrownBy(() -> userService.checkAvailableName(request)).isInstanceOf(ConflictException.class);
        }

        @AutoSource
        @ParameterizedTest
        void 중복되지_않은_닉네임이면_통과한다(String name) {
            // given
            CheckAvailableNameRequest request = CheckAvailableNameRequest.testInstance(name);

            // when & then
            userService.checkAvailableName(request);
        }

    }

    @Nested
    class 회원정보_수정 {

        @AutoSource
        @ParameterizedTest
        void 나의_회원정보를_수정시_해당_회원의_데이터가_수정된다(String socialId, UserSocialType socialType, String name) {
            // given
            User user = UserCreator.create(socialId, socialType, "기존의 닉네임");
            userRepository.save(user);

            UpdateUserInfoRequest request = UpdateUserInfoRequest.testInstance(name);

            // when
            userService.updateUserInfo(request, user.getId());

            // then
            List<User> users = userRepository.findAll();
            assertThat(users).hasSize(1);
            assertUser(users.get(0), socialId, socialType, name);
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

        @AutoSource
        @ParameterizedTest
        void 회원탈퇴시_유저의_백업데이터가_생성된다(String socialId, UserSocialType socialType, String name) {
            // given
            User user = UserCreator.create(socialId, socialType, name);
            userRepository.save(user);

            // when
            userService.signOut(user.getId());

            // then
            List<User> users = userRepository.findAll();
            assertThat(users).isEmpty();

            List<WithdrawalUser> withdrawalUsers = withdrawalUserRepository.findAll();
            assertThat(withdrawalUsers).hasSize(1);
            assertWithdrawalUser(withdrawalUsers.get(0), user);
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
            assertThat(users).hasSize(1);
            assertUser(users.get(0), user2.getSocialId(), user2.getSocialType(), user2.getName());
        }

        @Test
        void 존재하지_않는_유저를_회원탈퇴시_NotFound_에러가_발생한다() {
            // given
            Long notFoundUserId = -1L;

            // when & then
            assertThatThrownBy(() -> userService.signOut(notFoundUserId)).isInstanceOf(NotFoundException.class);
        }

    }

    @DisplayName("유저의 장착중인 대표 칭호를 변경한다")
    @Nested
    class ActivateUserMedal {

        @AutoSource
        @ParameterizedTest
        void 장착중인_대표_칭호를_변경한다(String name, String iconUrl) {
            // given
            Medal medal = MedalCreator.create(name, iconUrl);
            medalRepository.save(medal);

            User user = UserCreator.create("social-id", UserSocialType.KAKAO, name);
            user.addMedals(List.of(medal));
            userRepository.save(user);

            ActivateUserMedalRequest request = ActivateUserMedalRequest.testInstance(medal.getId());

            // when
            userService.activateUserMedal(request, user.getId());

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertUserMedal(userMedals.get(0), user.getId(), medal.getId(), UserMedalStatus.ACTIVE)
            );
        }

        @Test
        void 대표_칭호_변경시_보유하지_않은_칭호를_장착하려하면_NotFound_에러가_발생한다() {
            // given
            Long notFoundMedalId = -1L;
            ActivateUserMedalRequest request = ActivateUserMedalRequest.testInstance(notFoundMedalId);

            User user = UserCreator.create("social-id", UserSocialType.KAKAO, "강승호");
            userRepository.save(user);

            // when & then
            assertThatThrownBy(() -> userService.activateUserMedal(request, user.getId())).isInstanceOfAny(NotFoundException.class);
        }

        @AutoSource
        @ParameterizedTest
        void 대표_칭호_변경시_메달_종류를_NULL을_요청하면_대표_칭호가_비활성화된다(String name, String iconUrl) {
            // given
            Medal medal = MedalCreator.create(name, iconUrl);
            medalRepository.save(medal);

            User user = UserCreator.create("social-id", UserSocialType.KAKAO, name);
            user.addMedals(List.of(medal));
            userRepository.save(user);

            ActivateUserMedalRequest request = ActivateUserMedalRequest.testInstance(null);

            // when
            userService.activateUserMedal(request, user.getId());

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertUserMedal(userMedals.get(0), user.getId(), medal.getId(), UserMedalStatus.IN_ACTIVE)
            );
        }
    }

}
