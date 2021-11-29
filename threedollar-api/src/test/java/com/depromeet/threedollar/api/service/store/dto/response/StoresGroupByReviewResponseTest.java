package com.depromeet.threedollar.api.service.store.dto.response;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StoresGroupByReviewResponseTest {

    private static final int DISTANCE = 1000;

    @ValueSource(doubles = {0, 0.9, -1})
    @ParameterizedTest
    void ZERO_TO_ONE(double rating) {
        // given
        StoreWithVisitsAndDistanceResponse store = StoreWithVisitsAndDistanceResponse.testInstance(DISTANCE, rating);

        // when
        StoresGroupByReviewResponse response = StoresGroupByReviewResponse.of(List.of(store));

        // then
        assertThat(response.getStoreList0()).hasSize(1);
    }

    @ValueSource(doubles = {1, 1.9})
    @ParameterizedTest
    void ONE_TO_TWO(double rating) {
        // given
        StoreWithVisitsAndDistanceResponse store = StoreWithVisitsAndDistanceResponse.testInstance(DISTANCE, rating);

        // when
        StoresGroupByReviewResponse response = StoresGroupByReviewResponse.of(List.of(store));

        // then
        assertThat(response.getStoreList1()).hasSize(1);
    }

    @ValueSource(doubles = {2, 2.9})
    @ParameterizedTest
    void TWO_TO_THREE(double rating) {
        // given
        StoreWithVisitsAndDistanceResponse store = StoreWithVisitsAndDistanceResponse.testInstance(DISTANCE, rating);

        // when
        StoresGroupByReviewResponse response = StoresGroupByReviewResponse.of(List.of(store));

        // then
        assertThat(response.getStoreList2()).hasSize(1);
    }

    @ValueSource(doubles = {3, 3.9})
    @ParameterizedTest
    void THREE_TO_FOUR(double rating) {
        // given
        StoreWithVisitsAndDistanceResponse store = StoreWithVisitsAndDistanceResponse.testInstance(DISTANCE, rating);

        // when
        StoresGroupByReviewResponse response = StoresGroupByReviewResponse.of(List.of(store));

        // then
        assertThat(response.getStoreList3()).hasSize(1);
    }

    @ValueSource(doubles = {4, 5, 5.1})
    @ParameterizedTest
    void FOUR_TO_FIVE(double rating) {
        // given
        StoreWithVisitsAndDistanceResponse store = StoreWithVisitsAndDistanceResponse.testInstance(1000, rating);

        // when
        StoresGroupByReviewResponse response = StoresGroupByReviewResponse.of(List.of(store));

        // then
        assertThat(response.getStoreList4()).hasSize(1);
    }

}
