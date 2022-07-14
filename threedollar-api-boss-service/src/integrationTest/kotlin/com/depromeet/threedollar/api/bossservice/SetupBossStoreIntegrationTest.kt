package com.depromeet.threedollar.api.bossservice

import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.category.BossStoreCategoryCacheRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

internal abstract class SetupBossStoreIntegrationTest : SetupBossAccountIntegrationTest() {

    @Autowired
    protected lateinit var bossStoreRepository: BossStoreRepository

    @MockkBean
    private lateinit var bossStoreCategoryCacheRepository: BossStoreCategoryCacheRepository

    protected lateinit var bossStore: BossStore
    protected lateinit var bossStoreId: String

    @BeforeEach
    fun disableCacheCategories() {
        every { bossStoreCategoryCacheRepository.getCache() } returns null
    }

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
