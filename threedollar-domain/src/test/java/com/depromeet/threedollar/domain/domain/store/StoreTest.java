package com.depromeet.threedollar.domain.domain.store;

import com.depromeet.threedollar.domain.domain.common.DayOfTheWeek;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.menu.MenuCreator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class StoreTest {

    @Nested
    class UpdateAppearanceDays {

        @Test
        void 개시일을_수정한다() {
            // given
            Store store = StoreCreator.createWithDefaultMenu(100L, "가게");
            store.addAppearanceDays(Set.of(DayOfTheWeek.MONDAY));

            // when
            store.updateAppearanceDays(Set.of(DayOfTheWeek.SATURDAY, DayOfTheWeek.FRIDAY));

            // then
            assertAll(
                () -> assertThat(store.getAppearanceDays()).hasSize(2),
                () -> assertThat(store.getAppearanceDayTypes()).containsExactlyInAnyOrder(DayOfTheWeek.SATURDAY, DayOfTheWeek.FRIDAY)
            );
        }

        @Test
        void 개시일을_모두_삭제하는_경우() {
            // given
            Store store = StoreCreator.createWithDefaultMenu(100L, "가게");
            store.addAppearanceDays(Set.of(DayOfTheWeek.MONDAY));

            // when
            store.updateAppearanceDays(Collections.emptySet());

            // then
            assertAll(
                () -> assertThat(store.getAppearanceDays()).isEmpty()
            );
        }

    }

    @Nested
    class UpdatePaymentMethods {

        @Test
        void 결제방법을_수정한다() {
            // given
            Store store = StoreCreator.createWithDefaultMenu(100L, "가게");
            store.addPaymentMethods(Set.of(PaymentMethodType.CARD));

            // when
            store.updatePaymentMethods(Set.of(PaymentMethodType.CARD, PaymentMethodType.CASH));

            // then
            assertAll(
                () -> assertThat(store.getPaymentMethods()).hasSize(2),
                () -> assertThat(store.getPaymentMethodTypes()).containsExactlyInAnyOrder(PaymentMethodType.CARD, PaymentMethodType.CASH)
            );
        }

        @Test
        void 결제방법을_모두_삭제하는_경우() {
            // given
            Store store = StoreCreator.createWithDefaultMenu(100L, "가게");
            store.addPaymentMethods(Set.of(PaymentMethodType.CARD));

            // when
            store.updatePaymentMethods(Collections.emptySet());

            // then
            assertAll(
                () -> assertThat(store.getPaymentMethods()).isEmpty()
            );
        }

    }

    @Nested
    class HasMenuCategory {

        @Test
        void 가게에_해당하는_메뉴_카테고리_존재하면_true를_반환한다() {
            // given
            Store store = StoreCreator.create(100L, "가게");
            store.addMenus(List.of(MenuCreator.create(store, "name", "price", MenuCategoryType.BUNGEOPPANG)));

            // when
            boolean result = store.hasMenuCategory(MenuCategoryType.BUNGEOPPANG);

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 가게에_해당하는_메뉴_카테고리가_존재하지_않으면_false를_반환한다() {
            // given
            Store store = StoreCreator.create(100L, "가게");
            store.addMenus(List.of(MenuCreator.create(store, "name", "price", MenuCategoryType.BUNGEOPPANG)));

            // when
            boolean result = store.hasMenuCategory(MenuCategoryType.DALGONA);

            // then
            assertThat(result).isFalse();
        }

    }

    @Nested
    class GetMenuCategoriesSortedByCounts {

        @Test
        void 가게의_카테고리_조회시_가게_메뉴_카테고리의_수로_정렬해서_반환한다() {
            // given
            Store store = StoreCreator.create(100L, "가게");
            store.addMenus(List.of(
                MenuCreator.create(store, "name", "price", MenuCategoryType.BUNGEOPPANG),
                MenuCreator.create(store, "name", "price", MenuCategoryType.BUNGEOPPANG),
                MenuCreator.create(store, "name", "price", MenuCategoryType.EOMUK)
            ));

            // when
            List<MenuCategoryType> categories = store.getMenuCategoriesSortedByCounts();

            // then
            assertThat(categories.get(0)).isEqualTo(MenuCategoryType.BUNGEOPPANG);
            assertThat(categories.get(1)).isEqualTo(MenuCategoryType.EOMUK);
        }

        @Test
        void 가게에_아무런_메뉴도_없을경우_빈_리스트을_반환한다() {
            // given
            Store store = StoreCreator.create(100L, "가게");

            // when
            List<MenuCategoryType> categories = store.getMenuCategoriesSortedByCounts();

            // then
            assertThat(categories).isEmpty();
        }

    }

    @Nested
    class UpdateAverageRating {

        @CsvSource({
            "2.67, 2.7",
            "2.64, 2.6"
        })
        @ParameterizedTest
        void 가게의_평균점수를_갱신시_소수점_둘째자리에서_반올림된다(double rating, double expectedResult) {
            // given
            Store store = StoreCreator.create(100L, "가게 이름");

            // when
            store.updateAverageRating(rating);

            // then
            assertThat(store.getRating()).isEqualTo(expectedResult);
        }

    }

}
