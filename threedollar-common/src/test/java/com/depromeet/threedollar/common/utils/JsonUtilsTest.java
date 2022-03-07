package com.depromeet.threedollar.common.utils;

import com.depromeet.threedollar.common.model.CoordinateValue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

}
