package com.depromeet.threedollar.domain.rds.user.domain.store.projection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreStatus;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreWithMenuProjection;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreCreator;

class StoreWithMenuProjectionTest {

    @Nested
    class GetMenuCategoriesSortedByCountsTest {

        @Test
        void 가게의_카테고리_조회시_가게_메뉴의_수가_많은것부터_정렬해서_반환한다() {
            // given
            StoreWithMenuProjection.MenuProjection menu1 = StoreWithMenuProjection.MenuProjection.builder()
                .category(UserMenuCategoryType.BUNGEOPPANG)
                .price("1000원")
                .name("팥 붕어빵")
                .build();

            StoreWithMenuProjection.MenuProjection menu2 = StoreWithMenuProjection.MenuProjection.builder()
                .category(UserMenuCategoryType.BUNGEOPPANG)
                .price("1000원")
                .name("팥 붕어빵")
                .build();

            StoreWithMenuProjection.MenuProjection menu3 = StoreWithMenuProjection.MenuProjection.builder()
                .category(UserMenuCategoryType.EOMUK)
                .price("1000원")
                .name("어묵")
                .build();

            StoreWithMenuProjection store = StoreWithMenuProjection.builder()
                .userId(1000L)
                .latitude(34.0)
                .longitude(128.0)
                .name("가게")
                .rating(4.0)
                .status(StoreStatus.ACTIVE)
                .menus(List.of(menu1, menu2, menu3))
                .build();

            // when
            List<UserMenuCategoryType> categories = store.getMenuCategoriesSortedByCounts();

            // then
            assertThat(categories.get(0)).isEqualTo(UserMenuCategoryType.BUNGEOPPANG);
            assertThat(categories.get(1)).isEqualTo(UserMenuCategoryType.EOMUK);
        }

        @Test
        void 가게에_아무런_메뉴도_없을경우_빈_리스트을_반환한다() {
            // given
            Store store = StoreCreator.create(100L, "가게");

            // when
            List<UserMenuCategoryType> categories = store.getMenuCategoriesSortedByCounts();

            // then
            assertThat(categories).isEmpty();
        }

    }

}
