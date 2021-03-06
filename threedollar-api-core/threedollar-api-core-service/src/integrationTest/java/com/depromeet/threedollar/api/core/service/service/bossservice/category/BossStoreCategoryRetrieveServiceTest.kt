package com.depromeet.threedollar.api.core.service.service.bossservice.category

import com.depromeet.threedollar.api.core.service.IntegrationTest
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.category.BossStoreCategoryCacheRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class BossStoreCategoryRetrieveServiceTest(
    private val bossStoreCategoryRetrieveService: BossStoreCategoryRetrieveService,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreCategoryCacheRepository: BossStoreCategoryCacheRepository,
) : IntegrationTest() {

    @AfterEach
    fun cleanUp() {
        bossStoreCategoryRepository.deleteAll()
        bossStoreCategoryCacheRepository.cleanCache()
    }

    @Test
    fun `카테고리 목록이 캐시에 없는 경우, MongoDB에서 가져온 데이터를 Redis에 저장한다`() {
        // when
        bossStoreCategoryRetrieveService.retrieveBossStoreCategories()

        val bossStoreCategories = bossStoreCategoryCacheRepository.getCache()
        assertThat(bossStoreCategories).isNotNull
    }

    @Test
    fun `카테고리 ids에 해당하는 카테고리 목록을 조회할때, 카테고리 목록이 캐시에 없는 경우, MongoDB에서 가져온 데이터를 Redis에 저장한다`() {
        // given
        val category = BossStoreCategoryFixture.create(title = "한식", sequencePriority = 1, imageUrl = "https://icon.png")
        bossStoreCategoryRepository.save(category)

        // when
        bossStoreCategoryRetrieveService.retrieveBossStoreCategoriesByIds(listOf(category.id))

        // then
        val bossStoreCategories = bossStoreCategoryCacheRepository.getCache()
        assertThat(bossStoreCategories).isNotNull
    }

}
