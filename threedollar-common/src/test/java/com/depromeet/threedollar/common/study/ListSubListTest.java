package com.depromeet.threedollar.common.study;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ListSubListTest {

    @Test
    void subList_3부터_3까지_자르면_빈_리스트가_반환된다() {
        // given
        int size = 3;
        List<String> stringList = List.of("A", "B", "C");

        // when
        List<String> front = stringList.subList(0, size);
        List<String> back = stringList.subList(size, stringList.size());

        // then
        assertThat(front).hasSize(3);
        assertThat(back).isEmpty();
    }

    @Test
    void subList_SIZE_이상인경우_SIZE와나머지로_자른다() {
        // given
        int size = 3;
        List<String> stringList = List.of("A", "B", "C", "D", "E");

        // when
        List<String> front = stringList.subList(0, size);
        List<String> back = stringList.subList(size, stringList.size());

        // then
        assertThat(front).hasSize(3);
        assertThat(back).hasSize(2);
    }

}
