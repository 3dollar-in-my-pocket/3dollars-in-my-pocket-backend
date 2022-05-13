package com.depromeet.threedollar.api.user.service;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.depromeet.threedollar.domain.rds.user.domain.store.AppearanceDayRepository;
import com.depromeet.threedollar.common.type.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.PaymentMethodRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;

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
        store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
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
