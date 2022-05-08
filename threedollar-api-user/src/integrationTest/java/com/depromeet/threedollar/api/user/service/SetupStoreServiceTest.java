package com.depromeet.threedollar.api.user.service;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.depromeet.threedollar.domain.rds.user.domain.store.AppearanceDayRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.PaymentMethodRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreWithMenuCreator;

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
        store = StoreWithMenuCreator.builder()
            .userId(user.getId())
            .storeName("디프만 붕어빵")
            .build();
        store.addMenus(List.of(
            MenuCreator.builder()
                .store(store)
                .name("팥 붕어빵")
                .price("2개에 천원")
                .category(MenuCategoryType.BUNGEOPPANG)
                .build()
        ));
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
