package com.depromeet.threedollar.api.service.store.dto.response;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StoresGroupByDistanceResponseTest {

    private static final double RATING = 3;

    @ValueSource(ints = {0, 49, -1})
    @ParameterizedTest
    void UNDER_FIFTY(int distance) {
        // given
        StoreInfoResponse store = StoreInfoResponse.testInstance(distance, RATING);

        // when
        StoresGroupByDistanceResponse response = StoresGroupByDistanceResponse.of(List.of(store));

        // then
        assertThat(response.getStoreList50()).hasSize(1);
    }

    @ValueSource(ints = {50, 99})
    @ParameterizedTest
    void FIFTY_TO_HUNDRED(int distance) {
        // given
        StoreInfoResponse store = StoreInfoResponse.testInstance(distance, RATING);

        // when
        StoresGroupByDistanceResponse response = StoresGroupByDistanceResponse.of(List.of(store));

        // then
        assertThat(response.getStoreList100()).hasSize(1);
    }

    @ValueSource(ints = {100, 499})
    @ParameterizedTest
    void HUNDRED_TO_FIVE_HUNDRED(int distance) {
        // given
        StoreInfoResponse store = StoreInfoResponse.testInstance(distance, RATING);

        // when
        StoresGroupByDistanceResponse response = StoresGroupByDistanceResponse.of(List.of(store));

        // then
        assertThat(response.getStoreList500()).hasSize(1);
    }

    @ValueSource(ints = {500, 999})
    @ParameterizedTest
    void FIVE_HUNDRED_TO_THOUSAND(int distance) {
        // given
        StoreInfoResponse store = StoreInfoResponse.testInstance(distance, RATING);

        // when
        StoresGroupByDistanceResponse response = StoresGroupByDistanceResponse.of(List.of(store));

        // then
        assertThat(response.getStoreList1000()).hasSize(1);
    }

    @ValueSource(ints = {1000, 5000, 100000})
    @ParameterizedTest
    void OVER_THOUSAND(int distance) {
        // given
        StoreInfoResponse store = StoreInfoResponse.testInstance(distance, RATING);

        // when
        StoresGroupByDistanceResponse response = StoresGroupByDistanceResponse.of(List.of(store));

        // then
        assertThat(response.getStoreListOver1000()).hasSize(1);
    }

}