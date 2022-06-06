package com.depromeet.threedollar.api.userservice.service;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.AppearanceDayRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.MenuCreator;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.MenuRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.PaymentMethodRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreCreator;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;

public abstract class SetupStoreServiceTest extends SetupUserServiceTest {

    @Autowired
    protected StoreRepository storeRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected AppearanceDayRepository appearanceDayRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    protected Long storeId;

    protected Store store;

    @BeforeEach
    void setUpStore() {
        store = StoreCreator.create(userId, "디프만 붕어빵");
        store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", UserMenuCategoryType.BUNGEOPPANG)));
        storeRepository.save(store);
        storeId = store.getId();
    }

    @Override
    protected void cleanup() {
        super.cleanup();
        appearanceDayRepository.deleteAllInBatch();
        paymentMethodRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
    }

}