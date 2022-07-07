package com.depromeet.threedollar.common.utils;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PartitionUtilsTest {

    @Test
    void 리스트를_파티션으로_나눕니다() {
        // given
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        // when
        List<List<Integer>> partitions = PartitionUtils.partition(numbers, 3);

        // then
        assertAll(
            () -> assertThat(partitions).hasSize(2),
            () -> assertThat(partitions.get(0)).hasSize(3),
            () -> assertThat(partitions.get(0)).isEqualTo(List.of(1, 2, 3)),
            () -> assertThat(partitions.get(1)).hasSize(2),
            () -> assertThat(partitions.get(1)).isEqualTo(List.of(4, 5))
        );
    }

    @Test
    void 리스트의_개수가_파티션의_요소_개수보다_적은경우_한개의_파티션으로_구성된다() {
        // given
        List<Integer> numbers = List.of(1, 2);

        // when
        List<List<Integer>> partitions = PartitionUtils.partition(numbers, 3);

        // then
        assertAll(
            () -> assertThat(partitions).hasSize(1),
            () -> assertThat(partitions.get(0)).isEqualTo(List.of(1, 2))
        );
    }

    @Test
    void 빈_리스트인경우_빈_리스트_하나가_반환된다() {
        // given
        List<Integer> numbers = Collections.emptyList();

        // when
        List<List<Integer>> partitions = PartitionUtils.partition(numbers, 3);

        // then
        assertThat(partitions).isEmpty();
    }

}
