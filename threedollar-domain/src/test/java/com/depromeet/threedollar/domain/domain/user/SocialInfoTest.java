package com.depromeet.threedollar.domain.domain.user;

import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class SocialInfoTest {

    @Nested
    class 동등성_테스트 {

        @AutoSource
        @ParameterizedTest
        void 유저_소셜정보_동등성_테스트_모두_같으면_같은_객체로_판단(String socialId, UserSocialType socialType) {
            // given
            // when
            SocialInfo source = SocialInfo.of(socialId, socialType);
            SocialInfo target = SocialInfo.of(socialId, socialType);

            // then
            assertThat(source).isEqualTo(target);
        }

        @AutoSource
        @ParameterizedTest
        void 유저_소셜정보_동등성_테스트_하나라도_다른경우_다른_객체로_판단(String socialId) {
            // when
            SocialInfo source = SocialInfo.of(socialId, UserSocialType.APPLE);
            SocialInfo target = SocialInfo.of(socialId, UserSocialType.KAKAO);

            // then
            assertThat(source).isNotEqualTo(target);
        }

    }

}
