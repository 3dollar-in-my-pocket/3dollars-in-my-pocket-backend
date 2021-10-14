package com.depromeet.threedollar.domain.domain.menu;

import com.depromeet.threedollar.domain.domain.menu.repository.projection.MenuStaticsProjection;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        List<MenuStaticsProjection> result = menuRepository.countsGroupByMenu();

        // then
        assertThat(result).hasSize(2);
    }

}
