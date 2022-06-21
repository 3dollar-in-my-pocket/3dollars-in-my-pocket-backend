package com.depromeet.threedollar.api.core.service.bossservice.category

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import com.depromeet.threedollar.api.core.IntegrationTest
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.category.BossStoreCategoryCacheRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify

internal class BossStoreCategoryServiceTest(
    private val bossStoreCategoryService: BossStoreCategoryService,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
) : IntegrationTest() {

    @MockkBean
    private lateinit var bossStoreCategoryCacheRepository: BossStoreCategoryCacheRepository

    @AfterEach
    fun cleanUp() {
        bossStoreCategoryRepository.deleteAll()
    }

    @Test
    fun `카테고리 목록이 캐시에 없는 경우, MongoDB에서 가져온 데이터를 Redis에 저장한다`() {
        // given
        every { bossStoreCategoryCacheRepository.set(any()) } returns Unit
        every { bossStoreCategoryCacheRepository.getAll() } returns null

        // when
        bossStoreCategoryService.retrieveBossStoreCategories()

        // then
        verify(exactly = 1) { bossStoreCategoryCacheRepository.set(any()) }
    }

    @Test
    fun `카테고리 ids에 해당하는 카테고리 목록을 조회할때, 카테고리 목록이 캐시에 없는 경우, MongoDB에서 가져온 데이터를 Redis에 저장한다`() {
        // given
        every { bossStoreCategoryCacheRepository.set(any()) } returns Unit
        every { bossStoreCategoryCacheRepository.getAll() } returns null

        val category = BossStoreCategoryFixture.create(title = "한식", sequencePriority = 1)
        bossStoreCategoryRepository.save(category)

        // when
        bossStoreCategoryService.retrieveBossStoreCategoriesByIds(listOf(category.id))

        // then
        verify(exactly = 1) { bossStoreCategoryCacheRepository.set(any()) }
    }

}
