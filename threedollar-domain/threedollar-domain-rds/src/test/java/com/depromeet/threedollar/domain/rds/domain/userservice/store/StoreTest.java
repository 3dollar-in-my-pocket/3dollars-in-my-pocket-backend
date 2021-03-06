package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
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
    class UpdateAppearanceDaysTest {

        @Test
        void 가게의_영업일_정보를_수정한다() {
            // given
            Store store = StoreFixture.createWithDefaultMenu(100L);
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
        void 가게의_영업일_정보를_모두_삭제한다() {
            // given
            Store store = StoreFixture.createWithDefaultMenu(100L);
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
    class UpdatePaymentMethodsTest {

        @Test
        void 가게의_결제방법을_수정한다() {
            // given
            Store store = StoreFixture.createWithDefaultMenu(100L);
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
        void 가게의_결제방법을_모두_삭제한다() {
            // given
            Store store = StoreFixture.createWithDefaultMenu(100L);
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
    class HasMenuCategoryTest {

        @Test
        void 가게에_해당_카테고리를판매중인지_여부_확인시_메뉴_카테고리_존재하면_true를_반환한다() {
            // given
            Store store = StoreFixture.create();
            store.addMenus(List.of(MenuFixture.create(store, "name", "price", UserMenuCategoryType.BUNGEOPPANG)));

            // when
            boolean result = store.hasMenuCategory(UserMenuCategoryType.BUNGEOPPANG);

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 게에_해당_카테고리를판매중인지_여부_확인시_해당하는_메뉴_카테고리가_존재하지_않으면_false를_반환한다() {
            // given
            Store store = StoreFixture.create();
            store.addMenus(List.of(MenuFixture.create(store, "name", "price", UserMenuCategoryType.BUNGEOPPANG)));

            // when
            boolean result = store.hasMenuCategory(UserMenuCategoryType.DALGONA);

            // then
            assertThat(result).isFalse();
        }

    }

    @Nested
    class GetMenuCategoriesSortedByCountsTest {

        @Test
        void 가게의_카테고리_조회시_가게_메뉴의_수가_많은것부터_정렬해서_반환한다() {
            // given
            Store store = StoreFixture.create();
            store.addMenus(List.of(
                MenuFixture.create(store, "name", "price", UserMenuCategoryType.BUNGEOPPANG),
                MenuFixture.create(store, "name", "price", UserMenuCategoryType.BUNGEOPPANG),
                MenuFixture.create(store, "name", "price", UserMenuCategoryType.EOMUK)
            ));

            // when
            List<UserMenuCategoryType> categories = store.getMenuCategoriesSortedByCounts();

            // then
            assertThat(categories.get(0)).isEqualTo(UserMenuCategoryType.BUNGEOPPANG);
            assertThat(categories.get(1)).isEqualTo(UserMenuCategoryType.EOMUK);
        }

        @Test
        void 가게에_아무런_메뉴도_없을경우_빈_리스트을_반환한다() {
            // given
            Store store = StoreFixture.create();

            // when
            List<UserMenuCategoryType> categories = store.getMenuCategoriesSortedByCounts();

            // then
            assertThat(categories).isEmpty();
        }

    }

    @Nested
    class UpdateAverageReviewRatingTest {

        @CsvSource({
            "2.67, 2.7",
            "2.64, 2.6"
        })
        @ParameterizedTest
        void 가게의_평균점수를_갱신시_소수점_둘째자리에서_반올림된다(double rating, double expectedResult) {
            // given
            Store store = StoreFixture.create();

            // when
            store.updateAverageRating(rating);

            // then
            assertThat(store.getRating()).isEqualTo(expectedResult);
        }

    }

}
