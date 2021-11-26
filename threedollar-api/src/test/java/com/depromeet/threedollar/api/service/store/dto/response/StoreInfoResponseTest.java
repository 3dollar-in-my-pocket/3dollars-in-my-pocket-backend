package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.menu.MenuCreator;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreCreator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class StoreInfoResponseTest {

    @Test
    void 삭제된_가게인경우_삭제된_가게_이름으로_표기된다() {
        // given
        Store store = StoreCreator.createDeletedWithDefaultMenu(10000L, "가게 이름");

        // when
        StoreWithDistanceResponse response = StoreWithDistanceResponse.of(store, null, null, 0, 0);

        // then
        assertAll(
            () -> assertThat(response.getStoreName()).isEqualTo("삭제된 가게입니다"),
            () -> assertThat(response.getIsDeleted()).isTrue()
        );
    }

    @Test
    void 삭제된_가게인경우_이름을_제외한_나머지_정보들을_정상_반환한다() {
        // given
        Store store = StoreCreator.createDeleted(10000L, "가게 이름");
        store.addMenus(List.of(
            MenuCreator.create(store, "붕어빵", "천원", MenuCategoryType.BUNGEOPPANG),
            MenuCreator.create(store, "달고나", "2천원", MenuCategoryType.DALGONA)
        ));

        // when
        StoreWithDistanceResponse response = StoreWithDistanceResponse.of(store, null, null, 0, 0);

        // then
        assertAll(
            () -> assertThat(response.getStoreId()).isEqualTo(store.getId()),
            () -> assertThat(response.getRating()).isEqualTo(store.getRating()),
            () -> assertThat(response.getLatitude()).isEqualTo(store.getLatitude()),
            () -> assertThat(response.getLongitude()).isEqualTo(store.getLongitude()),
            () -> assertThat(response.getCategories()).containsExactlyInAnyOrder(MenuCategoryType.BUNGEOPPANG, MenuCategoryType.DALGONA)
        );
    }

    @Test
    void 따로_현재_위도와_경도를_넘기지_않는경우_거리는_0으로_표시된다() {
        // given
        Store store = StoreCreator.createWithDefaultMenu(10000L, "가게 이름");

        // when
        StoreWithDistanceResponse response = StoreWithDistanceResponse.of(store, null, null, 0, 0);

        // then
        assertThat(response.getDistance()).isEqualTo(0);
    }

}
