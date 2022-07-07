package com.depromeet.threedollar.common.utils;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.common.model.LocationValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonUtilsTest {

    @Test
    void 객체를_JSON으로_직렬화한다() {
        // given
        LocationValue locationValue = LocationValue.of(38.0, 128.0);

        // when
        String json = JsonUtils.toJson(locationValue);

        // then
        assertThat(json).isEqualTo("{\"latitude\":38.0,\"longitude\":128.0}");
    }

    @Test
    void JSON을_객체로_역직렬화한다() {
        // given
        String json = "{\"latitude\":38.0,\"longitude\":128.0}";

        // when
        LocationValue locationValue = JsonUtils.toObject(json, LocationValue.class);

        // then
        assertThat(locationValue).isEqualTo(LocationValue.of(38.0, 128.0));
    }

    @Test
    void JSON포맷이_아닌경우_throw_InternalServerException() {
        // given
        String json = "wrong json";

        // when & then
        assertThatThrownBy(() -> JsonUtils.toObject(json, LocationValue.class)).isInstanceOf(InternalServerException.class);
    }

    @Test
    void List를_JSON으로_변환한다() {
        // given
        List<LocationValue> locationValues = List.of(
            LocationValue.of(38.0, 128.0),
            LocationValue.of(35.0, 130.0)
        );

        // when
        String json = JsonUtils.toJson(locationValues);

        // then
        assertThat(json).isEqualTo("[{\"latitude\":38.0,\"longitude\":128.0},{\"latitude\":35.0,\"longitude\":130.0}]");
    }

    @Test
    void JSON을_List로_변환한다() {
        // given
        String json = "[{\"latitude\":36.0,\"longitude\":128.0},{\"latitude\":38.0,\"longitude\":130.0}]";

        // when
        List<LocationValue> locationValues = JsonUtils.toList(json, LocationValue.class);

        // then
        assertThat(locationValues).isEqualTo(List.of(
            LocationValue.of(36.0, 128.0),
            LocationValue.of(38.0, 130.0)
        ));
    }

}
