package com.depromeet.threedollar.domain.redis.boss.domain.store

import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class BossStoreOpenRedisRepository(
    private val bossStoreOpenRedisRepository: StringRedisRepository<BossStoreOpenRedisKey, LocalDateTime>
) {

    fun get(bossStoreId: String): LocalDateTime? {
        return bossStoreOpenRedisRepository.get(BossStoreOpenRedisKey(bossStoreId))
    }

    fun set(bossStoreId: String, openDateTime: LocalDateTime) {
        bossStoreOpenRedisRepository.set(BossStoreOpenRedisKey.of(bossStoreId), openDateTime)
    }

    fun delete(bossStoreId: String) {
        bossStoreOpenRedisRepository.delete(BossStoreOpenRedisKey.of(bossStoreId))
    }

}
