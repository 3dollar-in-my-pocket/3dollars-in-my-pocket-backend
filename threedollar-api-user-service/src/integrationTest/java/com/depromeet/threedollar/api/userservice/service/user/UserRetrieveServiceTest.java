package com.depromeet.threedollar.api.userservice.service.user;

import com.depromeet.threedollar.api.userservice.IntegrationTest;
import com.depromeet.threedollar.api.userservice.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserRetrieveServiceTest extends IntegrationTest {

    @Autowired
    private UserRetrieveService userRetrieveService;

    @Autowired
    private UserRepository userRepository;

    @Nested
    class GetUserAccountInfoTest {

        @Test
        void 유저_정보_조회시_존재하지_않는_유저인경우_NOTFOUNDEXCEPTION이_발생합니다() {
            // given
            Long notFoundUserId = -1L;

            // when & then
            assertThatThrownBy(() -> userRetrieveService.getUserInfo(notFoundUserId)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class CheckDuplicateNicknameTest {

        @Test
        void 닉네임_사용가능_확인시_중복된_닉네임인경우_CONFLICT_에러가_발생합니다() {
            // given
            String name = "토끼";
            User user = UserFixture.create("social-id", UserSocialType.KAKAO, name);
            userRepository.save(user);

            CheckAvailableNameRequest request = CheckAvailableNameRequest.testBuilder()
                .name(name)
                .build();

            // when & then
            assertThatThrownBy(() -> userRetrieveService.checkIsAvailableName(request)).isInstanceOf(ConflictException.class);
        }

        @Test
        void 닉네임_사용가능_확인시_중복되지_않는_닉네임인경우_통과합니다() {
            // given
            CheckAvailableNameRequest request = CheckAvailableNameRequest.testBuilder()
                .name("토끼")
                .build();

            // when & then
            assertDoesNotThrow(() -> userRetrieveService.checkIsAvailableName(request));
        }

    }

}
