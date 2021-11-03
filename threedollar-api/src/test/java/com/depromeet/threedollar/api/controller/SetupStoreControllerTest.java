package com.depromeet.threedollar.api.controller;

import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.menu.MenuCreator;
import com.depromeet.threedollar.domain.domain.menu.MenuRepository;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public class SetupStoreControllerTest extends SetupUserControllerTest {

    @Autowired
    protected StoreRepository storeRepository;

    @Autowired
    protected MenuRepository menuRepository;

    protected Long storeId;

    protected Store store;

    @BeforeEach
    void setUpStore() {
        store = StoreCreator.create(testUser.getId(), "디프만 붕어빵");
        store.addMenus(Collections.singletonList(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
        storeRepository.save(store);
        storeId = store.getId();
    }

    @Override
    protected void cleanup() {
        super.cleanup();
        menuRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
    }

}