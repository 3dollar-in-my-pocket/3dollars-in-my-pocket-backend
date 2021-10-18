package com.depromeet.threedollar.api.service;

import com.depromeet.threedollar.domain.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class StoreSetupTest extends UserSetUpTest {

    @Autowired
    protected StoreRepository storeRepository;

    protected Long storeId;

    @BeforeEach
    void setUpStore() {
        storeId = storeRepository.save(StoreCreator.create(userId, "테스트 가게")).getId();
    }

    @Override
    protected void cleanup() {
        super.cleanup();
        storeRepository.deleteAll();
    }

}
