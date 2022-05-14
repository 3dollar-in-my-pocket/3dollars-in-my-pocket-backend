package com.depromeet.threedollar.domain.rds.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.depromeet.threedollar.common.exception.model.InvalidException;

class EmailTest {

    @ValueSource(strings = {
        "will.seungho@gmail.com",
        "will.seungho@webtoonscrop.com",
        "ksh980212@gmail.com"
    })
    @ParameterizedTest
    void 이메일_포맷에_맞는경우_정규표현식을_통과한다(String email) {
        // when
        Email result = Email.of(email);

        // then
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @ValueSource(strings = {
        "will.seungho",
        "will.seungho@gmail",
        "@gmail.com",
        ""
    })
    @ParameterizedTest
    void 이메일_정규식_테스트_도메인_없는_주소인경우_실패한다(String email) {
        // when & then
        assertThatThrownBy(() -> Email.of(email)).isInstanceOf(InvalidException.class);
    }

}
