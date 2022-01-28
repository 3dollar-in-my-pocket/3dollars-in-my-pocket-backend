package com.depromeet.threedollar.domain.user.domain.store;

import com.depromeet.threedollar.domain.user.domain.store.projection.StoreWithReportedCountProjection;
import com.depromeet.threedollar.domain.user.domain.storedelete.DeleteReasonType;
import com.depromeet.threedollar.domain.user.domain.storedelete.StoreDeleteRequestCreator;
import com.depromeet.threedollar.domain.user.domain.storedelete.StoreDeleteRequestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreDeleteRequestRepository storeDeleteRequestRepository;

    @DisplayName("특정 반경 거리안에 있는 가게들을 조회한다")
    @Nested
    class findStoresByLocationLessThanDistance {

        @Test
        void 반경에_있는_가게들을_조회한다() {
            // given
            Store store = StoreCreator.createWithDefaultMenu(10000L, "storeName", 37.358483, 126.930947);
            storeRepository.save(store);

            // when
            List<Store> stores = storeRepository.findStoresByLocationLessThanDistance(37.358086, 126.933012, 2.0);

            // then
            assertThat(stores).hasSize(1);
            assertThat(stores).extracting(Store::getId).containsExactly(store.getId());
        }

        @Test
        void 반경에_있는_가게들을_조회할떄_아무_가게가_없는경우_빈_리스트를_반환한다() {
            // given
            Store store = StoreCreator.createWithDefaultMenu(10000L, "storeName", 37.328431, 126.91674);
            storeRepository.save(store);

            // when
            List<Store> stores = storeRepository.findStoresByLocationLessThanDistance(37.358086, 126.933012, 2.0);

            // then
            assertThat(stores).isEmpty();
        }

    }

    @DisplayName("내가 제보한 가게 수를 카운트한다")
    @Nested
    class findCountsByUserId {

        @Test
        void 메뉴가_없는_가게는_포함되지_않는다() {
            // given
            long userId = 10000L;
            storeRepository.save(StoreCreator.create(userId, "2번 가게"));

            // when
            long counts = storeRepository.findCountsByUserId(userId);

            // then
            assertThat(counts).isEqualTo(0);
        }

        @Test
        void 삭제된_가게도_포함된다() {
            // given
            long userId = 10000L;
            Store deletedStore = StoreCreator.createDeletedWithDefaultMenu(userId, "2번 가게");
            storeRepository.save(deletedStore);

            // when
            long counts = storeRepository.findCountsByUserId(userId);

            // then
            assertThat(counts).isEqualTo(1);
        }

    }

    @DisplayName("내가 제보한 가게들을 스크롤 페이지네이션으로 조회한다")
    @Nested
    class findAllByUserIdUsingCursor {

        @DisplayName("lastStoreId가 null이면 첫 스크롤 페이지를 조회한다")
        @Test
        void 사용자가_등록한_가게_첫페이지를_조회한다() {
            // given
            long userId = 100L;

            Store store1 = StoreCreator.createWithDefaultMenu(userId, "1번 가게");
            Store store2 = StoreCreator.createWithDefaultMenu(userId, "2번 가게");
            Store store3 = StoreCreator.createWithDefaultMenu(userId, "3번 가게");
            storeRepository.saveAll(List.of(store1, store2, store3));

            // when
            List<Store> stores = storeRepository.findAllByUserIdUsingCursor(userId, null, 2);

            // then
            assertThat(stores).hasSize(2);
            assertThat(stores.get(0).getName()).isEqualTo("3번 가게");
            assertThat(stores.get(1).getName()).isEqualTo("2번 가게");
        }

        @DisplayName("lastStoreId보다 이후에 생성된 가게들을 조회한다")
        @Test
        void 사용자가_등록한_가게_두번째_페이지를_조회한다() {
            // given
            long userId = 100L;

            Store store1 = StoreCreator.createWithDefaultMenu(userId, "1번 가게");
            Store store2 = StoreCreator.createWithDefaultMenu(userId, "2번 가게");
            Store store3 = StoreCreator.createWithDefaultMenu(userId, "3번 가게");
            storeRepository.saveAll(List.of(store1, store2, store3));

            // when
            List<Store> stores = storeRepository.findAllByUserIdUsingCursor(userId, store2.getId(), 2);

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
            Store store0 = StoreCreator.createWithDefaultMenu(100L, "0개 삭제 요청된 가게");
            Store store1 = StoreCreator.createWithDefaultMenu(100L, "1개 삭제 요청된 가게");
            Store store2 = StoreCreator.createWithDefaultMenu(100L, "2개 삭제 요청된 가게");
            Store store3 = StoreCreator.createWithDefaultMenu(100L, "3개 삭제 요청된 가게");
            storeRepository.saveAll(List.of(store0, store1, store2, store3));

            storeDeleteRequestRepository.saveAll(List.of(
                StoreDeleteRequestCreator.create(store1, 100L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store2, 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequestCreator.create(store2, 101L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store3, 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequestCreator.create(store3, 101L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store3, 102L, DeleteReasonType.NOSTORE)
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
            Store store1 = StoreCreator.createWithDefaultMenu(100L, "1개 삭제 요청된 가게");
            Store store2 = StoreCreator.createWithDefaultMenu(100L, "2개 삭제 요청된 가게");
            Store store3 = StoreCreator.createWithDefaultMenu(100L, "3개 삭제 요청된 가게");
            storeRepository.saveAll(List.of(store1, store2, store3));

            storeDeleteRequestRepository.saveAll(List.of(
                StoreDeleteRequestCreator.create(store1, 100L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store2, 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequestCreator.create(store2, 101L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store3, 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequestCreator.create(store3, 101L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store3, 102L, DeleteReasonType.NOSTORE)
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
            Store store1 = StoreCreator.createWithDefaultMenu(100L, "1개 삭제 요청된 가게");
            Store store2 = StoreCreator.createWithDefaultMenu(100L, "2개 삭제 요청된 가게");
            Store store3 = StoreCreator.createWithDefaultMenu(100L, "3개 삭제 요청된 가게");
            storeRepository.saveAll(List.of(store1, store2, store3));

            storeDeleteRequestRepository.saveAll(List.of(
                StoreDeleteRequestCreator.create(store1, 100L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store2, 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequestCreator.create(store2, 101L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store3, 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequestCreator.create(store3, 101L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store3, 102L, DeleteReasonType.NOSTORE)
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
