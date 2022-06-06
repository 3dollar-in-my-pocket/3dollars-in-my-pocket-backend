package com.depromeet.threedollar.api.boss.service

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.category.BossStoreCategoryCacheRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

internal abstract class SetupBossStoreServiceTest : SetupBossAccountServiceTest() {

    @Autowired
    protected lateinit var bossStoreRepository: BossStoreRepository

    @MockkBean
    private lateinit var bossStoreCategoryCacheRepository: BossStoreCategoryCacheRepository

    protected lateinit var bossStore: BossStore
    protected lateinit var bossStoreId: String

    @BeforeEach
    fun disableCacheCategories() {
        every { bossStoreCategoryCacheRepository.getAll() } returns null
    }

    @BeforeEach
    override fun setup() {
        super.setup()
        bossStore = bossStoreRepository.save(BossStoreCreator.create(
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
