package com.depromeet.threedollar.domain.domain.store;

import com.depromeet.threedollar.domain.domain.menu.Menu;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.menu.MenuCreator;
import com.depromeet.threedollar.domain.domain.storedelete.DeleteReasonType;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestCreator;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.domain.store.projection.StoreWithReportedCountProjection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreDeleteRequestRepository storeDeleteRequestRepository;

    @Nested
    class findStoresByLocationLessThanDistance {

        @Test
        void 반경_3KM의_가게들을_조회한다() {
            // given
            Store store = Store.builder()
                .userId(100L)
                .latitude(37.358483)
                .longitude(126.930947)
                .name("storeName")
                .type(StoreType.STORE)
                .build();
            store.addMenus(List.of(MenuCreator.create(store, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            // when
            List<Store> stores = storeRepository.findStoresByLocationLessThanDistance(37.358086, 126.933012, 2.0);

            // then
            assertThat(stores).hasSize(1);
        }

        @Test
        void 반경_3KM의_가게들을_조회할떄_아무_가게가_없는경우_빈_리스트를_반환한다() {
            // given
            Store store = Store.builder()
                .userId(100L)
                .latitude(37.328431)
                .longitude(126.91674)
                .name("storeName")
                .type(StoreType.STORE)
                .build();

            Menu menu = MenuCreator.create(store, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG);
            store.addMenus(List.of(menu));
            storeRepository.save(store);

            // when
            List<Store> stores = storeRepository.findStoresByLocationLessThanDistance(37.358086, 126.933012, 2.0);

            // then
            assertThat(stores).isEmpty();
        }

    }

    @Nested
    class findCountsByUserId {

        @Test
        void 사용자가_등록한_가게들의_총_개수를_조회한다() {
            // given
            Long userId = 100L;

            Store store1 = StoreCreator.create(userId, "1번 가게");
            store1.addMenus(List.of(MenuCreator.create(store1, "붕어빵1", "만원1", MenuCategoryType.BUNGEOPPANG)));
            store1.addMenus(List.of(MenuCreator.create(store1, "붕어빵2", "만원2", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(userId, "2번 가게");
            store2.addMenus(List.of(MenuCreator.create(store2, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));

            Store store3 = StoreCreator.create(userId, "3번 가게");
            store3.addMenus(List.of(MenuCreator.create(store3, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(List.of(store1, store2, store3));

            // when
            long count = storeRepository.findCountsByUserId(userId);

            // then
            assertThat(count).isEqualTo(3);
        }

        @Test
        void 메뉴가_없는_가게는_조회되지_않는다() {
            // given
            Long userId = 100L;

            Store store1 = StoreCreator.create(userId, "1번 가게");
            store1.addMenus(List.of(MenuCreator.create(store1, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(userId, "2번 가게");

            storeRepository.saveAll(List.of(store1, store2));

            // when
            long counts = storeRepository.findCountsByUserId(userId);

            // then
            assertThat(counts).isEqualTo(1);
        }

        @Test
        void 하나도_등록하지_않은경우_count는_0이된다() {
            // given
            Long userId = 100L;

            // when
            long counts = storeRepository.findCountsByUserId(userId);

            // then
            assertThat(counts).isEqualTo(0);
        }

    }

    @Nested
    class findAllByUserIdWithScroll {

        @Test
        void 사용자가_등록한_가게_첫페이지를_조회한다() {
            // given
            Long userId = 100L;

            Store store1 = StoreCreator.create(userId, "1번 가게");
            store1.addMenus(List.of(MenuCreator.create(store1, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(userId, "2번 가게");
            store2.addMenus((List.of(MenuCreator.create(store2, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));

            Store store3 = StoreCreator.create(userId, "3번 가게");
            store3.addMenus((List.of(MenuCreator.create(store3, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(List.of(store1, store2, store3));

            // when
            List<Store> stores = storeRepository.findAllByUserIdWithScroll(userId, null, 2);

            // then
            assertThat(stores).hasSize(2);
            assertThat(stores.get(0).getName()).isEqualTo("3번 가게");
            assertThat(stores.get(1).getName()).isEqualTo("2번 가게");
        }

        @Test
        void 사용자가_등록한_가게_두번째_페이지를_조회한다() {
            // given
            Long userId = 100L;

            Store store1 = StoreCreator.create(userId, "1번 가게");
            store1.addMenus(List.of(MenuCreator.create(store1, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(userId, "2번 가게");
            store2.addMenus(List.of(MenuCreator.create(store2, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));

            Store store3 = StoreCreator.create(userId, "3번 가게");
            store3.addMenus(List.of(MenuCreator.create(store3, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(List.of(store1, store2, store3));

            // when
            List<Store> stores = storeRepository.findAllByUserIdWithScroll(userId, store2.getId(), 2);

            // then
            assertThat(stores).hasSize(1);
            assertThat(stores.get(0).getName()).isEqualTo("1번 가게");
        }

    }

    @DisplayName("N개 이상 삭제 요청된 가게들을 페이지네이션으로 조회한다")
    @Nested
    class findStoresByMoreThanReportCntWithPagination {

        @Test
        void N개_이상_삭제_요청된_가게들을_조회한다() {
            // given
            Store store0 = StoreCreator.create(100L, "0개 삭제 요청된 가게");
            Store store1 = StoreCreator.create(100L, "1개 삭제 요청된 가게");
            Store store2 = StoreCreator.create(100L, "2개 삭제 요청된 가게");
            Store store3 = StoreCreator.create(100L, "3개 삭제 요청된 가게");
            storeRepository.saveAll(List.of(store0, store1, store2, store3));

            storeDeleteRequestRepository.saveAll(List.of(
                StoreDeleteRequestCreator.create(store1.getId(), 100L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store2.getId(), 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequestCreator.create(store2.getId(), 101L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store3.getId(), 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequestCreator.create(store3.getId(), 101L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store3.getId(), 102L, DeleteReasonType.NOSTORE)
            ));

            // when
            List<StoreWithReportedCountProjection> stores = storeRepository.findStoresByMoreThanReportCntWithPagination(2, 0, 3);

            // then
            assertThat(stores).hasSize(2);
            assertStoreDeleteRequestReportDto(stores.get(0), store3.getId(), store3.getName(), store3.getLatitude(), store3.getLongitude(), store3.getType(), store3.getRating(), 3);
            assertStoreDeleteRequestReportDto(stores.get(1), store2.getId(), store2.getName(), store2.getLatitude(), store2.getLongitude(), store2.getType(), store2.getRating(), 2);
        }

        @DisplayName("삭제 요청된 수가 많은 것 부터 SIZE 만큼 잘라서 조회한다: 1페이지")
        @Test
        void 페이지네이션_1페이지를_조회한다() {
            // given
            Store store1 = StoreCreator.create(100L, "1개 삭제 요청된 가게");
            Store store2 = StoreCreator.create(100L, "2개 삭제 요청된 가게");
            Store store3 = StoreCreator.create(100L, "3개 삭제 요청된 가게");
            storeRepository.saveAll(List.of(store1, store2, store3));

            storeDeleteRequestRepository.saveAll(List.of(
                StoreDeleteRequestCreator.create(store1.getId(), 100L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store2.getId(), 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequestCreator.create(store2.getId(), 101L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store3.getId(), 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequestCreator.create(store3.getId(), 101L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store3.getId(), 102L, DeleteReasonType.NOSTORE)
            ));

            // when
            List<StoreWithReportedCountProjection> stores = storeRepository.findStoresByMoreThanReportCntWithPagination(1, 0, 2);

            // then
            assertThat(stores).hasSize(2);
            assertStoreDeleteRequestReportDto(stores.get(0), store3.getId(), store3.getName(), store3.getLatitude(), store3.getLongitude(), store3.getType(), store3.getRating(), 3);
            assertStoreDeleteRequestReportDto(stores.get(1), store2.getId(), store2.getName(), store2.getLatitude(), store2.getLongitude(), store2.getType(), store2.getRating(), 2);
        }

        @DisplayName("삭제 요청된 수가 많은 것 부터 SIZE 만큼 잘라서 조회한다: 2페이지")
        @Test
        void 페이지네이션_2페이지를_조회한다() {
            // given
            Store store1 = StoreCreator.create(100L, "1개 삭제 요청된 가게");
            Store store2 = StoreCreator.create(100L, "2개 삭제 요청된 가게");
            Store store3 = StoreCreator.create(100L, "3개 삭제 요청된 가게");
            storeRepository.saveAll(List.of(store1, store2, store3));

            storeDeleteRequestRepository.saveAll(List.of(
                StoreDeleteRequestCreator.create(store1.getId(), 100L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store2.getId(), 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequestCreator.create(store2.getId(), 101L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store3.getId(), 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequestCreator.create(store3.getId(), 101L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store3.getId(), 102L, DeleteReasonType.NOSTORE)
            ));

            // when
            List<StoreWithReportedCountProjection> stores = storeRepository.findStoresByMoreThanReportCntWithPagination(1, 1, 2);

            // then
            assertThat(stores).hasSize(1);
            assertStoreDeleteRequestReportDto(stores.get(0), store1.getId(), store1.getName(), store1.getLatitude(), store1.getLongitude(), store1.getType(), store1.getRating(), 1);
        }

    }

    private void assertStoreDeleteRequestReportDto(StoreWithReportedCountProjection response, Long storeId, String name, double latitude, double longitude, StoreType type, double rating, int cnt) {
        assertThat(response.getStoreId()).isEqualTo(storeId);
        assertThat(response.getStoreName()).isEqualTo(name);
        assertThat(response.getLatitude()).isEqualTo(latitude);
        assertThat(response.getLongitude()).isEqualTo(longitude);
        assertThat(response.getType()).isEqualTo(type);
        assertThat(response.getRating()).isEqualTo(rating);
        assertThat(response.getReportsCount()).isEqualTo(cnt);
    }

}
