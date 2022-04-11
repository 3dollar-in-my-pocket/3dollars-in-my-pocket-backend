package com.depromeet.threedollar.common.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.common.model.CoordinateValue;

class JsonUtilsTest {

    @Test
    void 객체를_JSON으로_직렬화한다() {
        // given
        CoordinateValue coordinateValue = CoordinateValue.of(38.0, 128.0);

        // when
        String json = JsonUtils.toJson(coordinateValue);

        // then
        assertThat(json).isEqualTo("{\"latitude\":38.0,\"longitude\":128.0}");
    }

    @Test
    void JSON을_객체로_역직렬화한다() {
        // given
        String json = "{\"latitude\":38.0,\"longitude\":128.0}";

        // when
        CoordinateValue coordinateValue = JsonUtils.toObject(json, CoordinateValue.class);

        // then
        assertThat(coordinateValue).isEqualTo(CoordinateValue.of(38.0, 128.0));
    }

    @Test
    void JSON포맷이_아닌경우_throw_InternalServerException() {
        // given
        String json = "wrong json";

        // when & then
        assertThatThrownBy(() -> JsonUtils.toObject(json, CoordinateValue.class)).isInstanceOf(InternalServerException.class);
    }

}
