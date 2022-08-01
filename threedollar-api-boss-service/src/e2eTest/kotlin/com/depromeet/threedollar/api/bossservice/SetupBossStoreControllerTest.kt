package com.depromeet.threedollar.api.bossservice

import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

internal abstract class SetupBossStoreControllerTest : SetupBossAccountControllerTest() {

    @Autowired
    protected lateinit var bossStoreRepository: BossStoreRepository

    protected lateinit var bossStore: BossStore

    @BeforeEach
    protected fun setupBossStore() {
        bossStore = bossStoreRepository.save(BossStoreFixture.create(bossId = bossId))
    }

    override fun cleanup() {
        super.cleanup()
        bossAccountRepository.deleteAll()
    }

}
