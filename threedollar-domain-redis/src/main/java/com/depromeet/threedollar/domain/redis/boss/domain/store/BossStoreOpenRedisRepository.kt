package com.depromeet.threedollar.domain.redis.boss.domain.store

import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class BossStoreOpenRedisRepository(
    private val bossStoreOpenRedisRepository: StringRedisRepository<BossStoreOpenRedisKey, LocalDateTime>
) {

    fun get(bossStoreId: String): LocalDateTime? {
        val key = BossStoreOpenRedisKey(bossStoreId)
        return bossStoreOpenRedisRepository.get(key)
    }

    fun set(bossStoreId: String, openDateTime: LocalDateTime) {
        val key = BossStoreOpenRedisKey.of(bossStoreId)
        bossStoreOpenRedisRepository.set(key, openDateTime)
    }

    fun delete(bossStoreId: String) {
        val key = BossStoreOpenRedisKey.of(bossStoreId)
        bossStoreOpenRedisRepository.del(key)
    }

}
