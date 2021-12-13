package com.depromeet.threedollar.domain.domain.medal;

import com.depromeet.threedollar.domain.domain.medal.projection.MedalCountsStatisticsProjection;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserCreator;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class MedalRepositoryTest {

    @Autowired
    private MedalRepository medalRepository;

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

    private void assertMedalCountsStaticsProjection(MedalCountsStatisticsProjection projection, Medal medal, int counts) {
        assertAll(
            () -> assertThat(projection.getMedalName()).isEqualTo(medal.getName()),
            () -> assertThat(projection.getCounts()).isEqualTo(counts)
        );
    }

}
