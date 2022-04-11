package com.depromeet.threedollar.api.boss.service

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository

internal abstract class SetupBossStoreServiceTest : SetupBossAccountServiceTest() {

    @Autowired
    protected lateinit var bossStoreRepository: BossStoreRepository

    protected lateinit var bossStoreId: String

    @BeforeEach
    override fun setup() {
        super.setup()
        bossStoreId = bossStoreRepository.save(BossStoreCreator.create(
            bossId = bossId,
            name = "사장님의 가게 이름"
        )).id
    }

    override fun cleanup() {
        super.cleanup()
        bossStoreRepository.deleteAll()
    }

}
