package com.depromeet.threedollar.api.service;

import com.depromeet.threedollar.domain.domain.store.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
