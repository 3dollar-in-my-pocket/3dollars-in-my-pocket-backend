package com.depromeet.threedollar.api.service;

import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class StoreSetupTest extends UserSetUpTest {

    @Autowired
    protected StoreRepository storeRepository;

    protected Long storeId;

    protected Store store;

    @BeforeEach
    void setUpStore() {
        store = storeRepository.save(StoreCreator.create(userId, "테스트 가게"));
        storeId = store.getId();
    }

    @Override
    protected void cleanup() {
        super.cleanup();
        storeRepository.deleteAll();
    }

}
