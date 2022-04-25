package com.depromeet.threedollar.domain.rds.user.domain.visit;

import com.depromeet.threedollar.domain.rds.user.domain.store.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VisitHistoryRepositoryTest {

    private static final long USER_ID = 100000L;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @Autowired
    private StoreRepository storeRepository;

    @AfterEach
    void cleanUp() {
        visitHistoryRepository.deleteAllInBatch();
        storeRepository.deleteAll();
    }

    @Test
    void 해당_카테고리를_판매중인_가게에_방문한_횟수를_센다() {
        // given
        Store bungeoppangStore = StoreCreator.create(USER_ID, "붕어빵 가게");
        bungeoppangStore.addMenus(List.of(MenuCreator.create(bungeoppangStore, "팥 붕어빵", "천원", MenuCategoryType.BUNGEOPPANG)));

        Store dalgonaStore = StoreCreator.create(USER_ID, "달고나 가게");
        dalgonaStore.addMenus(List.of(MenuCreator.create(dalgonaStore, "달고나", "천원", MenuCategoryType.DALGONA)));

        storeRepository.saveAll(List.of(bungeoppangStore, dalgonaStore));

        VisitHistory visitHistoryBungeoppangStore = VisitHistoryCreator.create(bungeoppangStore, USER_ID, VisitType.EXISTS, LocalDate.of(2021, 12, 1));
        VisitHistory visitHistoryDalgonaStore = VisitHistoryCreator.create(dalgonaStore, USER_ID, VisitType.EXISTS, LocalDate.of(2021, 12, 1));
        visitHistoryRepository.saveAll(List.of(visitHistoryBungeoppangStore, visitHistoryDalgonaStore));

        // when
        long counts = visitHistoryRepository.countByUserIdAndMenuCategoryType(USER_ID, MenuCategoryType.BUNGEOPPANG);

        // then
        assertThat(counts).isEqualTo(1);
    }

    @Test
    void 카테고리를_따로_넘기지_않으면_모든_카테고리에_대한_가게_방문_횟수를_센다() {
        // given
        Store bungeoppangStore = StoreCreator.create(USER_ID, "붕어빵 가게");
        bungeoppangStore.addMenus(List.of(MenuCreator.create(bungeoppangStore, "팥 붕어빵", "천원", MenuCategoryType.BUNGEOPPANG)));

        Store dalgonaStore = StoreCreator.create(USER_ID, "달고나 가게");
        dalgonaStore.addMenus(List.of(MenuCreator.create(dalgonaStore, "달고나", "천원", MenuCategoryType.DALGONA)));

        storeRepository.saveAll(List.of(bungeoppangStore, dalgonaStore));

        VisitHistory visitHistoryBungeoppangStore = VisitHistoryCreator.create(bungeoppangStore, USER_ID, VisitType.EXISTS, LocalDate.of(2021, 12, 1));
        VisitHistory visitHistoryDalgonaStore = VisitHistoryCreator.create(dalgonaStore, USER_ID, VisitType.EXISTS, LocalDate.of(2021, 12, 1));
        visitHistoryRepository.saveAll(List.of(visitHistoryBungeoppangStore, visitHistoryDalgonaStore));

        // when
        long counts = visitHistoryRepository.countByUserIdAndMenuCategoryType(USER_ID, null);

        // then
        assertThat(counts).isEqualTo(2);
    }

}
