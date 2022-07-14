package com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen

import com.depromeet.threedollar.domain.mongo.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class BossStoreOpenRepositoryTest(
    private val bossStoreOpenRepository: BossStoreOpenRepository,
) : IntegrationTest() {

    @Test
    fun `커서가 null인 경우, 맨 처음 N개를 조회한다`() {
        // given
        val one = BossStoreOpenFixture.create("bossStoreId1", LocalDateTime.now(), LocalDateTime.now().plusHours(1))
        val two = BossStoreOpenFixture.create("bossStoreId2", LocalDateTime.now(), LocalDateTime.now().plusHours(1))
        val three = BossStoreOpenFixture.create("bossStoreId3", LocalDateTime.now(), LocalDateTime.now().plusHours(1))
        bossStoreOpenRepository.saveAll(listOf(one, two, three))

        // when
        val bossStores = bossStoreOpenRepository.findAllLessThanCursorLimit(cursor = null, limit = 2)

        // then
        assertThat(bossStores).hasSize(2)
        assertThat(bossStores[0].id).isEqualTo(three.id)
        assertThat(bossStores[1].id).isEqualTo(two.id)
    }

    @Test
    fun `커서가 있는 경우, 해당 커서 이전의 N개를 조회한다`() {
        // given
        val one = BossStoreOpenFixture.create("bossStoreId1", LocalDateTime.now(), LocalDateTime.now().plusHours(1))
        val two = BossStoreOpenFixture.create("bossStoreId2", LocalDateTime.now(), LocalDateTime.now().plusHours(1))
        val three = BossStoreOpenFixture.create("bossStoreId3", LocalDateTime.now(), LocalDateTime.now().plusHours(1))
        bossStoreOpenRepository.saveAll(listOf(one, two, three))

        // when
        val bossStores = bossStoreOpenRepository.findAllLessThanCursorLimit(cursor = three.id, limit = 2)

        // then
        assertThat(bossStores).hasSize(2)
        assertThat(bossStores[0].id).isEqualTo(two.id)
        assertThat(bossStores[1].id).isEqualTo(one.id)
    }

}
