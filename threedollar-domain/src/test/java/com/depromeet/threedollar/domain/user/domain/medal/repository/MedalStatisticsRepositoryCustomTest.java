package com.depromeet.threedollar.domain.user.domain.medal.repository;

import com.depromeet.threedollar.domain.user.domain.medal.*;
import com.depromeet.threedollar.domain.user.domain.medal.projection.MedalCountsStatisticsProjection;
import com.depromeet.threedollar.domain.user.domain.user.User;
import com.depromeet.threedollar.domain.user.domain.user.UserCreator;
import com.depromeet.threedollar.domain.user.domain.user.UserRepository;
import com.depromeet.threedollar.domain.user.domain.user.UserSocialType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MedalStatisticsRepositoryCustomTest {

    @Autowired
    private MedalRepository medalRepository;

    @Autowired
    private UserMedalRepository userMedalRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 통계용_유저가_보유한_메달의_갯수를_조회한다() {
        // given
        Medal medalOne = MedalCreator.create("붕어빵 챌린지");
        Medal medalTwo = MedalCreator.create("달고나 토끼");
        medalRepository.saveAll(List.of(medalOne, medalTwo));

        User userHasTwoMedals = UserCreator.create("social-id1", UserSocialType.KAKAO, "닉네임 1");
        userHasTwoMedals.addMedals(List.of(medalOne, medalTwo));

        User userHasMedal = UserCreator.create("social-id2", UserSocialType.GOOGLE, "닉네임 2");
        userHasMedal.addMedals(List.of(medalOne));

        userRepository.saveAll(List.of(userHasMedal, userHasTwoMedals));

        // when
        List<MedalCountsStatisticsProjection> result = medalRepository.findUserMedalsCountsGroupByMedal();

        // then
        assertAll(
            () -> assertThat(result).hasSize(2),
            () -> assertMedalCountsStaticsProjection(result.get(0), medalOne, 2),
            () -> assertMedalCountsStaticsProjection(result.get(1), medalTwo, 1)
        );
    }

    @Test
    void 메달별_유저가_장착중인_갯수를_조회한다() {
        // given
        Medal medalOne = MedalCreator.create("붕어빵 전문가");
        Medal medalTwo = MedalCreator.create("토끼네 호떡");
        Medal medalThree = MedalCreator.create("토끼네 달고나");
        medalRepository.saveAll(List.of(medalOne, medalTwo, medalThree));

        User user = UserCreator.create("social1", UserSocialType.KAKAO, "will");
        User user2 = UserCreator.create("social2", UserSocialType.GOOGLE, "rabbit");
        userRepository.saveAll(List.of(user, user2));

        UserMedal userMedalOne = UserMedalCreator.createActive(medalOne, user);
        UserMedal userMedalTwo = UserMedalCreator.createActive(medalTwo, user2);
        userMedalRepository.saveAll(List.of(userMedalOne, userMedalTwo));

        // when
        var result = medalRepository.findActiveCountsGroupByMedal();

        // then
        assertAll(
            () -> assertThat(result).hasSize(2),
            () -> assertMedalCountsStaticsProjection(result.get(0), medalOne, 1),
            () -> assertMedalCountsStaticsProjection(result.get(1), medalTwo, 1)
        );
    }

    private void assertMedalCountsStaticsProjection(MedalCountsStatisticsProjection projection, Medal medal, int counts) {
        assertAll(
            () -> assertThat(projection.getMedalName()).isEqualTo(medal.getName()),
            () -> assertThat(projection.getCounts()).isEqualTo(counts)
        );
    }

}
