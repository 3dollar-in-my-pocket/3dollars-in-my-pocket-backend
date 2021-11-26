package com.depromeet.threedollar.domain.domain.store;

import com.depromeet.threedollar.domain.domain.store.projection.MenuStatisticsProjection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void 메뉴_카테고리별로_등록된_수를_조회한다() {
        // given
        Store store = StoreCreator.create(100L, "가게");
        storeRepository.save(store);

        menuRepository.saveAll(List.of(
            MenuCreator.create(store, "붕어빵1", "가격", MenuCategoryType.BUNGEOPPANG),
            MenuCreator.create(store, "붕어빵2", "가격", MenuCategoryType.BUNGEOPPANG),
            MenuCreator.create(store, "달고나1", "가격", MenuCategoryType.DALGONA)
        ));

        // when
        List<MenuStatisticsProjection> result = menuRepository.countsGroupByMenu();

        // then
        assertAll(
            () -> assertThat(result).hasSize(2),
            () -> assertMenuStatisticsProjection(result.get(0), MenuCategoryType.BUNGEOPPANG, 2),
            () -> assertMenuStatisticsProjection(result.get(1), MenuCategoryType.DALGONA, 1)
        );
    }

    private void assertMenuStatisticsProjection(MenuStatisticsProjection projection, MenuCategoryType categoryType, long counts) {
        assertAll(
            () -> assertThat(projection.getCategory()).isEqualTo(categoryType),
            () -> assertThat(projection.getCounts()).isEqualTo(counts)
        );
    }

}
