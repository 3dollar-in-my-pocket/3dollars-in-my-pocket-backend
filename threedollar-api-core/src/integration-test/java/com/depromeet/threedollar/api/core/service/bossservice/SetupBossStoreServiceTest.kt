package com.depromeet.threedollar.api.core.service.bossservice

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository

internal abstract class SetupBossStoreServiceTest : SetupBossAccountAndUserServiceTest() {

    @Autowired
    protected lateinit var bossStoreRepository: BossStoreRepository

    protected lateinit var bossStoreId: String

    @BeforeEach
    override fun setup() {
        super.setup()
        bossStoreId = bossStoreRepository.save(
            BossStoreFixture.create(
            bossId = bossId,
            name = "사장님의 가게 이름"
        )).id
    }

    override fun cleanup() {
        super.cleanup()
        bossStoreRepository.deleteAll()
    }

}
