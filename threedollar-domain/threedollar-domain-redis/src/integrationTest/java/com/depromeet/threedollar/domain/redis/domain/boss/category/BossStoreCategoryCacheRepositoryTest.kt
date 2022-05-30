package com.depromeet.threedollar.domain.redis.domain.boss.category

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
class BossStoreCategoryCacheRepositoryTest(
    private val bossStoreCategoryCacheRepository: BossStoreCategoryCacheRepository,
    private val stringRedisRepository: StringRedisRepository<BossStoreCategoriesCacheKey, List<BossStoreCategoryCacheModel>>,
) {

    @AfterEach
    fun cleanUp() {
        stringRedisRepository.del(BossStoreCategoriesCacheKey())
    }

    @Test
    fun `사장님 가게 카테고리 목록이 캐시에 없는 경우 null을 반환한다`() {
        // when
        val categories = bossStoreCategoryCacheRepository.getAll()

        // then
        assertThat(categories).isNull()
    }

    @Test
    fun `사장님 가게 카테고리 목록을 조회한다`() {
        // given
        val category1 = BossStoreCategoryCacheModel(categoryId = "categoryId1", name = "한식")
        val category2 = BossStoreCategoryCacheModel(categoryId = "categoryId2", name = "중식")
        stringRedisRepository.set(BossStoreCategoriesCacheKey(), listOf(category1, category2))

        // when
        val categories = bossStoreCategoryCacheRepository.getAll()

        assertThat(categories).isNotNull
        assertThat(categories).hasSize(2)
        assertCacheModel(model = categories!![0], categoryId = category1.categoryId, name = category1.name)
        assertCacheModel(model = categories[1], categoryId = category2.categoryId, name = category2.name)
    }

    @Test
    fun `사장님 가게 카테고리 목록을 저장한다`() {
        // given
        val category = BossStoreCategoryCacheModel(categoryId = "categoryId1", name = "한식")

        // when
        bossStoreCategoryCacheRepository.set(listOf(category))

        // then
        val result = stringRedisRepository.get(BossStoreCategoriesCacheKey())
        assertThat(result).isNotNull
        assertThat(result).hasSize(1)
        assertCacheModel(model = result!![0], categoryId = category.categoryId, name = category.name)
    }

    private fun assertCacheModel(model: BossStoreCategoryCacheModel, categoryId: String, name: String) {
        assertThat(model.categoryId).isEqualTo(categoryId)
        assertThat(model.name).isEqualTo(name)
    }

}
