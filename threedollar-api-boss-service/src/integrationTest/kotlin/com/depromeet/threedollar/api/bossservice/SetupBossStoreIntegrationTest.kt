package com.depromeet.threedollar.api.bossservice

import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

internal abstract class SetupBossStoreIntegrationTest : SetupBossAccountIntegrationTest() {

    @Autowired
    protected lateinit var bossStoreRepository: BossStoreRepository

    protected lateinit var bossStore: BossStore
    protected lateinit var bossStoreId: String

    @BeforeEach
    override fun setup() {
        super.setup()
        bossStore = bossStoreRepository.save(BossStoreFixture.create(
            bossId = bossId,
            name = "사장님의 가게 이름"
        ))
        bossStoreId = bossStore.id
    }

    override fun cleanup() {
        super.cleanup()
        bossStoreRepository.deleteAll()
    }

}
