package com.depromeet.threedollar.domain.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 전체_회원수를_조회한다() {
        // given
        User user1 = UserCreator.create("social-id1", UserSocialType.APPLE, "유저 1");
        User user2 = UserCreator.create("social-id2", UserSocialType.APPLE, "유저 2");
        userRepository.saveAll(List.of(user1, user2));

        // when
        long result = userRepository.findUsersCount();

        // then
        assertThat(result).isEqualTo(2);
    }

    @ParameterizedTest
    @MethodSource("source_date")
    void 특정_날짜에_회원가입한_유저의_카운트를_조회한다(LocalDate startDate, LocalDate endDate, long expectedCount) {
        // given
        User user1 = UserCreator.create("social-id1", UserSocialType.APPLE, "유저 1");
        User user2 = UserCreator.create("social-id2", UserSocialType.APPLE, "유저 2");
        userRepository.saveAll(List.of(user1, user2));

        // when
        long counts = userRepository.findUsersCountByDate(startDate, endDate);

        // then
        assertThat(counts).isEqualTo(expectedCount);
    }

    private static Stream<Arguments> source_date() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate tomorrow = today.plusDays(1);
        return Stream.of(
            Arguments.of(today, today, 2),
            Arguments.of(yesterday, today, 2),
            Arguments.of(yesterday, yesterday, 0),
            Arguments.of(today, tomorrow, 2),
            Arguments.of(tomorrow, tomorrow, 0)
        );
    }

}
